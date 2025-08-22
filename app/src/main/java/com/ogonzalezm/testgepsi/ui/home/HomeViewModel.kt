package com.ogonzalezm.testgepsi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ogonzalezm.testgepsi.domain.model.SearchParams
import com.ogonzalezm.testgepsi.domain.repository.item.ItemRepository
import com.ogonzalezm.testgepsi.domain.repository.search.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val searchRepository: SearchRepository
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val submit = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class)
    val items =
        submit.receiveAsFlow()
            .map {
                SearchParams(
                    keyword = _state.value.keyword,
                    page = 1,
                    pageSize = 20,
                    sortBy = _state.value.sortBy
                )
            }
            .distinctUntilChanged()
            .flatMapLatest { params: SearchParams ->
                itemRepository.searchItems(params)
            }
            .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val recentSearches: StateFlow<List<String>> =
        state
            .map { it.keyword }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                Timber.e(query)
                searchRepository.getRecentSearchesByKeyword(query)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    fun handleIntent(intent: HomeIntent) {
        when(intent) {
            is HomeIntent.EnterKeyword -> _state.update { it.copy(keyword = intent.keyword, errorMessage = "") }
            HomeIntent.Search -> { search() }
            is HomeIntent.ChangeSort -> _state.update { it.copy(sortBy = it.sortBy) }
            HomeIntent.Clear -> { clear() }
            is HomeIntent.SearchByKeyword -> {
                _state.update { it.copy(keyword = intent.keyword) }
                search()
            }
        }
    }

    private fun search() {
        val keyword = _state.value.keyword
        if(keyword.isBlank()) {
            _state.update { it.copy(errorMessage = "Keyword must not be empty.") }
            return
        }
        submit.trySend(Unit)
        viewModelScope.launch {
            runCatching { searchRepository.add(keyword) }
                .onFailure { Timber.e(it) }
        }
    }

    private fun clear() {
        viewModelScope.launch {
            runCatching { searchRepository.deleteAll() }
                .onFailure { Timber.e(it) }
        }
    }

}
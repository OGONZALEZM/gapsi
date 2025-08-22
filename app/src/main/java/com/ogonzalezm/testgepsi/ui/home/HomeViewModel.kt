package com.ogonzalezm.testgepsi.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ogonzalezm.testgepsi.domain.model.Item
import com.ogonzalezm.testgepsi.domain.model.SearchParams
import com.ogonzalezm.testgepsi.domain.repository.item.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val itemRepository: ItemRepository
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

    fun handleIntent(intent: HomeIntent) {
        when(intent) {
            is HomeIntent.EnterKeyword -> _state.update { it.copy(keyword = intent.keyword, errorMessage = "") }
            HomeIntent.Search -> { search() }
            is HomeIntent.ChangeSort -> _state.update { it.copy(sortBy = it.sortBy) }
        }
    }

    fun search() {
        if(_state.value.keyword.isBlank()) {
            _state.update { it.copy(errorMessage = "Keyword must not be empty.") }
            return
        }
        Log.e("Home", "search")
        submit.trySend(Unit)
    }

//    private fun items(keyword: String, sortBy: String): Flow<PagingData<Item>> =
//        itemRepository.searchItems(
//            SearchParams(keyword = keyword, page = 1, pageSize = 20, sortBy = sortBy)
//        ).cachedIn(viewModelScope)

}
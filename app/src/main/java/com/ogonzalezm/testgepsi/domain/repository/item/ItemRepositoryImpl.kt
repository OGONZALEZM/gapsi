package com.ogonzalezm.testgepsi.domain.repository.item

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ogonzalezm.testgepsi.domain.model.SearchParams
import com.ogonzalezm.testgepsi.data.remote.ApiService
import com.ogonzalezm.testgepsi.data.remote.ItemsPagingSource
import com.ogonzalezm.testgepsi.domain.model.Item
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    val apiService: ApiService
): ItemRepository {

    override fun searchItems(params: SearchParams): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = params.pageSize,
                prefetchDistance = 20,
                enablePlaceholders = false,
                initialLoadSize = params.pageSize * 2
            ),
            pagingSourceFactory = {
                ItemsPagingSource(apiService, params)
            }
        ).flow
    }

}
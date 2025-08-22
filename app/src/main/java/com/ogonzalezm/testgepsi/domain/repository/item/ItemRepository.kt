package com.ogonzalezm.testgepsi.domain.repository.item

import androidx.paging.PagingData
import com.ogonzalezm.testgepsi.domain.model.SearchParams
import com.ogonzalezm.testgepsi.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    fun searchItems(params: SearchParams): Flow<PagingData<Item>>

}
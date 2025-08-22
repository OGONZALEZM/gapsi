package com.ogonzalezm.testgepsi.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ogonzalezm.testgepsi.domain.model.DataResponse
import com.ogonzalezm.testgepsi.domain.model.Item
import com.ogonzalezm.testgepsi.domain.model.SearchParams

class ItemsPagingSource(
    private val apiService: ApiService,
    private val searchParams: SearchParams
) : PagingSource<Int, Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> = try {
        val page = params.key ?: 1
        val req = searchParams.copy(page = page)

        Log.e("Paging", req.toString())

        val resp = safeApiCall {
            apiService.searchItems(req.keyword, req.page, req.sortBy)
        }

        when (resp) {
            is Resource.Error -> LoadResult.Error(Exception(resp.message))
            is Resource.Success<*> -> {
                val data = (resp.data as DataResponse)
                    .item?.props?.pageProps?.initialData?.searchResult
                    ?.itemStacks
                    ?.firstOrNull()
                    ?.items.orEmpty()

                val nextKey = if (data.isEmpty()) null else page + 1

                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextKey
                )
            }
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)
        return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
    }
}

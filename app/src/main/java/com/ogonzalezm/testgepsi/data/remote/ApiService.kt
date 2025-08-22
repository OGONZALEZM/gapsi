package com.ogonzalezm.testgepsi.data.remote

import com.ogonzalezm.testgepsi.domain.model.DataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("walmart-search-by-keyword")
    suspend fun searchItems(
        @Query("keyword") keyword: String,
        @Query("page") page: Int,
        @Query("sortBy") sortBy: String
    ): Response<DataResponse>

}
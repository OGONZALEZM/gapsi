package com.ogonzalezm.testgepsi.domain.repository.search

import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getRecentSearches(limit: Int = 10): Flow<List<String>>

    fun getRecentSearchesByKeyword(keyword: String, limit: Int = 10): Flow<List<String>>

    suspend fun add(keyword: String): Long

    suspend fun deleteAll()

}
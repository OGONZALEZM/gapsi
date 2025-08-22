package com.ogonzalezm.testgepsi.domain.repository.search

import com.ogonzalezm.testgepsi.database.dao.RecentSearchDao
import com.ogonzalezm.testgepsi.domain.model.RecentSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val recentSearchDao: RecentSearchDao
) : SearchRepository {

    override fun getRecentSearches(limit: Int) =
        recentSearchDao.getRecentSearches(limit).map { it.map(RecentSearch::query) }

    override fun getRecentSearchesByKeyword(
        keyword: String,
        limit: Int
    ): Flow<List<String>> = recentSearchDao.getRecentSearchesByKeyword(keyword, limit)
        .map { it.map(RecentSearch::query) }

    override suspend fun add(keyword: String) = withContext(Dispatchers.IO) {
        if (keyword.isBlank()) return@withContext -1L
        recentSearchDao.insert(RecentSearch(query = keyword))
    }

    override suspend fun deleteAll() {
        recentSearchDao.deleteAll()
    }

}
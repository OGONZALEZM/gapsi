package com.ogonzalezm.testgepsi.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ogonzalezm.testgepsi.domain.model.RecentSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: RecentSearch): Long

    @Query("SELECT * FROM recent_search ORDER BY id DESC LIMIT :limit")
    fun getRecentSearches(limit: Int = 10): Flow<List<RecentSearch>>

    @Query("SELECT * FROM recent_search WHERE `query` LIKE '%' || :keyword || '%' ORDER BY id DESC LIMIT :limit")
    fun getRecentSearchesByKeyword(keyword: String, limit: Int = 10): Flow<List<RecentSearch>>

    @Query("DELETE FROM recent_search")
    suspend fun deleteAll()

}
package com.ogonzalezm.testgepsi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ogonzalezm.testgepsi.database.dao.RecentSearchDao
import com.ogonzalezm.testgepsi.domain.model.RecentSearch

@Database(
    entities = [RecentSearch::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recentSearchDao(): RecentSearchDao

    companion object {
        const val DATABASE_NAME = "gapsi.db"
    }
}
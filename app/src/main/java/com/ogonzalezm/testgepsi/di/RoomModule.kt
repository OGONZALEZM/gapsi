package com.ogonzalezm.testgepsi.di

import android.content.Context
import androidx.room.Room
import com.ogonzalezm.testgepsi.database.AppDatabase
import com.ogonzalezm.testgepsi.database.dao.RecentSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .build()
    }

    @Provides
    fun provideRecentSearchDao(
        appDatabase: AppDatabase
    ): RecentSearchDao = appDatabase.recentSearchDao()

}
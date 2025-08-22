package com.ogonzalezm.testgepsi.di

import com.ogonzalezm.testgepsi.domain.repository.item.ItemRepository
import com.ogonzalezm.testgepsi.domain.repository.item.ItemRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideBookRepository(
        itemRepositoryImpl: ItemRepositoryImpl
    ): ItemRepository

}
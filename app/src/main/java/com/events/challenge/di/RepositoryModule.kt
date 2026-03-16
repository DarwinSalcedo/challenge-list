package com.events.challenge.di

import com.events.challenge.data.DefaultItemRepository
import com.events.challenge.domain.ItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindItemRepository(
        itemRepositoryImpl: DefaultItemRepository
    ): ItemRepository
}

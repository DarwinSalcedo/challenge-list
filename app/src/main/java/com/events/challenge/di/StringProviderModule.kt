package com.events.challenge.di

import com.events.challenge.core.DefaultStringProvider
import com.events.challenge.core.StringProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StringProviderModule {
    @Binds
    abstract fun bindStringProvider(
        stringProvider: DefaultStringProvider
    ): StringProvider
}

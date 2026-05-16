package dev.shorthouse.coinwatch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.common.SystemTimeProvider
import dev.shorthouse.coinwatch.common.TimeProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimeProviderModule {

    @Provides
    @Singleton
    fun provideTimeProvider(): TimeProvider {
        return SystemTimeProvider()
    }
}

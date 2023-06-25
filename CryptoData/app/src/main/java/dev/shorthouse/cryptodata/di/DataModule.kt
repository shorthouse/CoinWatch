package dev.shorthouse.cryptodata.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.cryptodata.data.repository.CoinRepository
import dev.shorthouse.cryptodata.data.repository.CoinRepositoryImpl
import dev.shorthouse.cryptodata.data.source.remote.CoinApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideCoinRepository(api: CoinApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }
}

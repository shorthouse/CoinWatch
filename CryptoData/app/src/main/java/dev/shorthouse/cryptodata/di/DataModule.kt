package dev.shorthouse.cryptodata.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.cryptodata.data.repository.CoinRepository
import dev.shorthouse.cryptodata.data.repository.CoinRepositoryImpl
import dev.shorthouse.cryptodata.data.source.remote.CryptocurrencyApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideCryptocurrencyRepository(api: CryptocurrencyApi): CoinRepository {
        return CoinRepositoryImpl(api)
    }
}

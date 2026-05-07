package dev.shorthouse.coinwatch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.data.source.remote.CoinApi
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataSourceModule {

    @Provides
    @Singleton
    fun provideCoinNetworkDataSource(coinApi: CoinApi): CoinNetworkDataSource {
        return CoinNetworkDataSourceImpl(coinApi = coinApi)
    }
}

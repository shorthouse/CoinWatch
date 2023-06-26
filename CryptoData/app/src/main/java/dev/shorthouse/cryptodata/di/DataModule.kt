package dev.shorthouse.cryptodata.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.cryptodata.data.repository.detail.CoinDetailRepository
import dev.shorthouse.cryptodata.data.repository.detail.CoinDetailRepositoryImpl
import dev.shorthouse.cryptodata.data.repository.list.CoinListItemRepository
import dev.shorthouse.cryptodata.data.repository.list.CoinListItemRepositoryImpl
import dev.shorthouse.cryptodata.data.source.remote.CoinApi
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideCoinNetworkDataSource(coinApi: CoinApi): CoinNetworkDataSource {
        return CoinNetworkDataSource(coinApi = coinApi)
    }

    @Provides
    @Singleton
    fun provideCoinListItemRepository(coinApi: CoinApi): CoinListItemRepository {
        return CoinListItemRepositoryImpl(coinApi = coinApi)
    }

    @Provides
    @Singleton
    fun provideCoinDetailRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinDetailRepository {
        return CoinDetailRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            ioDispatcher = ioDispatcher
        )
    }
}

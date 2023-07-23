package dev.shorthouse.cryptodata.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.cryptodata.data.repository.chart.CoinChartRepository
import dev.shorthouse.cryptodata.data.repository.chart.CoinChartRepositoryImpl
import dev.shorthouse.cryptodata.data.repository.coin.CoinRepository
import dev.shorthouse.cryptodata.data.repository.coin.CoinRepositoryImpl
import dev.shorthouse.cryptodata.data.repository.detail.CoinDetailRepository
import dev.shorthouse.cryptodata.data.repository.detail.CoinDetailRepositoryImpl
import dev.shorthouse.cryptodata.data.repository.marketStats.MarketStatsRepository
import dev.shorthouse.cryptodata.data.repository.marketStats.MarketStatsRepositoryImpl
import dev.shorthouse.cryptodata.data.source.remote.CoinApi
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataModule {
    @Provides
    @Singleton
    fun provideCoinNetworkDataSource(coinApi: CoinApi): CoinNetworkDataSource {
        return CoinNetworkDataSource(coinApi = coinApi)
    }

    @Singleton
    @Provides
    fun provideCoinRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinRepository {
        return CoinRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            ioDispatcher = ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideCoinDetailRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinDetailRepository {
        return CoinDetailRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            ioDispatcher = ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideCoinChartRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinChartRepository {
        return CoinChartRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            ioDispatcher = ioDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideMarketStatsRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): MarketStatsRepository {
        return MarketStatsRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            ioDispatcher = ioDispatcher
        )
    }
}

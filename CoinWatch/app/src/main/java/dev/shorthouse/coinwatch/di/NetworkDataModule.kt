package dev.shorthouse.coinwatch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.data.repository.chart.CoinChartRepository
import dev.shorthouse.coinwatch.data.repository.chart.CoinChartRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.detail.CoinDetailRepository
import dev.shorthouse.coinwatch.data.repository.detail.CoinDetailRepositoryImpl
import dev.shorthouse.coinwatch.data.source.remote.CoinApi
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSourceImpl
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object NetworkDataModule {

    @Provides
    @Singleton
    fun provideCoinRepository(
        coinNetworkDataSource: CoinNetworkDataSourceImpl,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinRepository {
        return CoinRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCoinDetailRepository(
        coinNetworkDataSource: CoinNetworkDataSourceImpl,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinDetailRepository {
        return CoinDetailRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCoinChartRepository(
        coinNetworkDataSource: CoinNetworkDataSourceImpl,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinChartRepository {
        return CoinChartRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCoinNetworkDataSource(coinApi: CoinApi): CoinNetworkDataSourceImpl {
        return CoinNetworkDataSourceImpl(coinApi = coinApi)
    }
}

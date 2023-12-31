package dev.shorthouse.coinwatch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.data.mapper.CoinChartMapper
import dev.shorthouse.coinwatch.data.mapper.CoinDetailsMapper
import dev.shorthouse.coinwatch.data.mapper.CoinMapper
import dev.shorthouse.coinwatch.data.mapper.CoinSearchResultsMapper
import dev.shorthouse.coinwatch.data.repository.cachedCoin.CachedCoinRepository
import dev.shorthouse.coinwatch.data.repository.cachedCoin.CachedCoinRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.chart.CoinChartRepository
import dev.shorthouse.coinwatch.data.repository.chart.CoinChartRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.details.CoinDetailsRepository
import dev.shorthouse.coinwatch.data.repository.details.CoinDetailsRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.searchResults.CoinSearchResultsRepository
import dev.shorthouse.coinwatch.data.repository.searchResults.CoinSearchResultsRepositoryImpl
import dev.shorthouse.coinwatch.data.source.local.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCoinRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        coinMapper: CoinMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinRepository {
        return CoinRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            ioDispatcher = ioDispatcher,
            coinMapper = coinMapper
        )
    }

    @Provides
    @Singleton
    fun provideCachedCoinRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        coinLocalDataSource: CoinLocalDataSource,
        coinMapper: CoinMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CachedCoinRepository {
        return CachedCoinRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinLocalDataSource = coinLocalDataSource,
            coinMapper = coinMapper,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideFavouriteCoinRepository(
        coinLocalDataSource: CoinLocalDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): FavouriteCoinRepository {
        return FavouriteCoinRepositoryImpl(
            coinLocalDataSource = coinLocalDataSource,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCoinDetailsRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        coinDetailsMapper: CoinDetailsMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinDetailsRepository {
        return CoinDetailsRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinDetailsMapper = coinDetailsMapper,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCoinChartRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        coinChartMapper: CoinChartMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinChartRepository {
        return CoinChartRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinChartMapper = coinChartMapper,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCoinSearchResultsRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        coinSearchResultsMapper: CoinSearchResultsMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoinSearchResultsRepository {
        return CoinSearchResultsRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinSearchResultsMapper = coinSearchResultsMapper,
            ioDispatcher = ioDispatcher
        )
    }
}

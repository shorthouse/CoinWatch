package dev.shorthouse.coinwatch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.common.TimeProvider
import dev.shorthouse.coinwatch.data.mapper.CoinChartMapper
import dev.shorthouse.coinwatch.data.mapper.CoinDetailsMapper
import dev.shorthouse.coinwatch.data.mapper.CoinMapper
import dev.shorthouse.coinwatch.data.mapper.CoinSearchResultsMapper
import dev.shorthouse.coinwatch.data.mapper.FearGreedMapper
import dev.shorthouse.coinwatch.data.mapper.FavouriteCoinMapper
import dev.shorthouse.coinwatch.data.mapper.GlobalMarketMapper
import dev.shorthouse.coinwatch.data.mapper.MarketStatsMapper
import dev.shorthouse.coinwatch.data.mapper.TrendingCoinMapper
import dev.shorthouse.coinwatch.data.repository.chart.CoinChartRepository
import dev.shorthouse.coinwatch.data.repository.chart.CoinChartRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.details.CoinDetailsRepository
import dev.shorthouse.coinwatch.data.repository.details.CoinDetailsRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.feargreed.FearGreedRepository
import dev.shorthouse.coinwatch.data.repository.feargreed.FearGreedRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.favouriteCoinId.FavouriteCoinIdRepository
import dev.shorthouse.coinwatch.data.repository.favouriteCoinId.FavouriteCoinIdRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.globalmarket.GlobalMarketRepository
import dev.shorthouse.coinwatch.data.repository.globalmarket.GlobalMarketRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.marketStats.MarketStatsRepository
import dev.shorthouse.coinwatch.data.repository.marketStats.MarketStatsRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.reviewprompt.ReviewPromptRepository
import dev.shorthouse.coinwatch.data.repository.reviewprompt.ReviewPromptRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.searchResults.CoinSearchResultsRepository
import dev.shorthouse.coinwatch.data.repository.searchResults.CoinSearchResultsRepositoryImpl
import dev.shorthouse.coinwatch.data.repository.trendingcoins.TrendingCoinsRepository
import dev.shorthouse.coinwatch.data.repository.trendingcoins.TrendingCoinsRepositoryImpl
import dev.shorthouse.coinwatch.data.source.local.database.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics.ReviewAnalyticsLocalDataSource
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
        coinLocalDataSource: CoinLocalDataSource,
        coinMapper: CoinMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): CoinRepository {
        return CoinRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinLocalDataSource = coinLocalDataSource,
            coinMapper = coinMapper,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideFavouriteCoinRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        coinLocalDataSource: CoinLocalDataSource,
        favouriteCoinMapper: FavouriteCoinMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): FavouriteCoinRepository {
        return FavouriteCoinRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinLocalDataSource = coinLocalDataSource,
            favouriteCoinMapper = favouriteCoinMapper,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideFavouriteCoinIdRepository(
        coinLocalDataSource: CoinLocalDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): FavouriteCoinIdRepository {
        return FavouriteCoinIdRepositoryImpl(
            coinLocalDataSource = coinLocalDataSource,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCoinDetailsRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        coinDetailsMapper: CoinDetailsMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
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
        timeProvider: TimeProvider,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): CoinChartRepository {
        return CoinChartRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinChartMapper = coinChartMapper,
            timeProvider = timeProvider,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCoinSearchResultsRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        coinSearchResultsMapper: CoinSearchResultsMapper,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): CoinSearchResultsRepository {
        return CoinSearchResultsRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinSearchResultsMapper = coinSearchResultsMapper,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun providesMarketStatsRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        marketStatsMapper: MarketStatsMapper,
    ): MarketStatsRepository {
        return MarketStatsRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            marketStatsMapper = marketStatsMapper
        )
    }

    @Provides
    @Singleton
    fun provideFearGreedRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        fearGreedMapper: FearGreedMapper,
    ): FearGreedRepository {
        return FearGreedRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            fearGreedMapper = fearGreedMapper
        )
    }

    @Provides
    @Singleton
    fun provideGlobalMarketRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        globalMarketMapper: GlobalMarketMapper,
    ): GlobalMarketRepository {
        return GlobalMarketRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            globalMarketMapper = globalMarketMapper
        )
    }

    @Provides
    @Singleton
    fun provideTrendingCoinsRepository(
        coinNetworkDataSource: CoinNetworkDataSource,
        trendingCoinMapper: TrendingCoinMapper,
    ): TrendingCoinsRepository {
        return TrendingCoinsRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            trendingCoinMapper = trendingCoinMapper
        )
    }

    @Provides
    @Singleton
    fun provideReviewPromptRepository(
        reviewAnalyticsLocalDataSource: ReviewAnalyticsLocalDataSource,
        timeProvider: TimeProvider,
    ): ReviewPromptRepository {
        return ReviewPromptRepositoryImpl(
            reviewAnalyticsLocalDataSource = reviewAnalyticsLocalDataSource,
            timeProvider = timeProvider,
        )
    }
}

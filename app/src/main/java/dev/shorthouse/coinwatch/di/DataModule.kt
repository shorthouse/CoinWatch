package dev.shorthouse.coinwatch.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.common.Constants.COIN_DATABASE_NAME
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
import dev.shorthouse.coinwatch.data.source.local.CachedCoinDao
import dev.shorthouse.coinwatch.data.source.local.CoinDatabase
import dev.shorthouse.coinwatch.data.source.local.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.FavouriteCoinDao
import dev.shorthouse.coinwatch.data.source.remote.CoinApi
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSourceImpl
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideCoinNetworkDataSource(coinApi: CoinApi): CoinNetworkDataSource {
        return CoinNetworkDataSourceImpl(coinApi = coinApi)
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
    fun provideCoinLocalDataSource(
        favouriteCoinDao: FavouriteCoinDao,
        cachedCoinDao: CachedCoinDao
    ): CoinLocalDataSource {
        return CoinLocalDataSource(
            favouriteCoinDao = favouriteCoinDao,
            cachedCoinDao = cachedCoinDao
        )
    }

    @Provides
    @Singleton
    fun provideFavouriteCoinDao(database: CoinDatabase): FavouriteCoinDao {
        return database.favouriteCoinDao()
    }

    @Provides
    @Singleton
    fun providedCachedCoinDao(database: CoinDatabase): CachedCoinDao {
        return database.cachedCoinDao()
    }

    @Provides
    @Singleton
    fun provideCoinDatabase(@ApplicationContext context: Context): CoinDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CoinDatabase::class.java,
            COIN_DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCoinRepository(
        coinNetworkDataSource: CoinNetworkDataSourceImpl,
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
    fun provideCoinDetailsRepository(
        coinNetworkDataSource: CoinNetworkDataSourceImpl,
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
        coinNetworkDataSource: CoinNetworkDataSourceImpl,
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
        coinNetworkDataSource: CoinNetworkDataSourceImpl,
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

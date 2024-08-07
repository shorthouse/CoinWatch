package dev.shorthouse.coinwatch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.data.source.local.database.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.database.CoinLocalDataSourceImpl
import dev.shorthouse.coinwatch.data.source.local.database.dao.CoinDao
import dev.shorthouse.coinwatch.data.source.local.database.dao.FavouriteCoinDao
import dev.shorthouse.coinwatch.data.source.local.database.dao.FavouriteCoinIdDao
import dev.shorthouse.coinwatch.data.source.remote.CoinApi
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideCoinNetworkDataSource(coinApi: CoinApi): CoinNetworkDataSource {
        return CoinNetworkDataSourceImpl(coinApi = coinApi)
    }

    @Provides
    @Singleton
    fun provideCoinLocalDataSource(
        favouriteCoinIdDao: FavouriteCoinIdDao,
        coinDao: CoinDao,
        favouriteCoinDao: FavouriteCoinDao
    ): CoinLocalDataSource {
        return CoinLocalDataSourceImpl(
            favouriteCoinIdDao = favouriteCoinIdDao,
            coinDao = coinDao,
            favouriteCoinDao = favouriteCoinDao
        )
    }
}

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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    @Provides
    @Singleton
    fun provideCoinLocalDataSource(
        favouriteCoinIdDao: FavouriteCoinIdDao,
        coinDao: CoinDao,
        favouriteCoinDao: FavouriteCoinDao,
    ): CoinLocalDataSource {
        return CoinLocalDataSourceImpl(
            favouriteCoinIdDao = favouriteCoinIdDao,
            coinDao = coinDao,
            favouriteCoinDao = favouriteCoinDao
        )
    }
}

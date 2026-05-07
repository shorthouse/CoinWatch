package dev.shorthouse.coinwatch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.data.source.local.database.CoinDatabase
import dev.shorthouse.coinwatch.data.source.local.database.dao.CoinDao
import dev.shorthouse.coinwatch.data.source.local.database.dao.FavouriteCoinDao
import dev.shorthouse.coinwatch.data.source.local.database.dao.FavouriteCoinIdDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideCoinDao(database: CoinDatabase): CoinDao {
        return database.coinDao()
    }

    @Provides
    @Singleton
    fun provideFavouriteCoinDao(database: CoinDatabase): FavouriteCoinDao {
        return database.favouriteCoinDao()
    }

    @Provides
    @Singleton
    fun provideFavouriteCoinIdDao(database: CoinDatabase): FavouriteCoinIdDao {
        return database.favouriteCoinIdDao()
    }
}

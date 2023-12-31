package dev.shorthouse.coinwatch.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.common.Constants
import dev.shorthouse.coinwatch.data.source.local.CachedCoinDao
import dev.shorthouse.coinwatch.data.source.local.CoinDatabase
import dev.shorthouse.coinwatch.data.source.local.FavouriteCoinDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

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
            Constants.COIN_DATABASE_NAME
        ).build()
    }
}

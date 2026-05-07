package dev.shorthouse.coinwatch.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.common.Constants
import dev.shorthouse.coinwatch.data.source.local.database.CoinDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCoinDatabase(@ApplicationContext context: Context): CoinDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CoinDatabase::class.java,
            Constants.COIN_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}

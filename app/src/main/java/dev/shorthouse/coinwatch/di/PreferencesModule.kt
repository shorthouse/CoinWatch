package dev.shorthouse.coinwatch.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.shorthouse.coinwatch.data.preferences.favourites.FavouritesPreferences
import dev.shorthouse.coinwatch.data.preferences.favourites.FavouritesPreferencesSerializer
import dev.shorthouse.coinwatch.data.preferences.global.UserPreferences
import dev.shorthouse.coinwatch.data.preferences.global.UserPreferencesSerializer
import dev.shorthouse.coinwatch.data.preferences.market.MarketPreferences
import dev.shorthouse.coinwatch.data.preferences.market.MarketPreferencesSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { appContext.dataStoreFile("user_preferences.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserPreferences() }
            )
        )
    }

    @Provides
    @Singleton
    fun provideFavouritesPreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<FavouritesPreferences> {
        return DataStoreFactory.create(
            serializer = FavouritesPreferencesSerializer,
            produceFile = { appContext.dataStoreFile("favourites_preferences.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { FavouritesPreferences() }
            )
        )
    }

    @Provides
    @Singleton
    fun provideMarketPreferences(
        @ApplicationContext appContext: Context
    ): DataStore<MarketPreferences> {
        return DataStoreFactory.create(
            serializer = MarketPreferencesSerializer,
            produceFile = { appContext.dataStoreFile("market_preferences.pb") },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { MarketPreferences() }
            )
        )
    }
}

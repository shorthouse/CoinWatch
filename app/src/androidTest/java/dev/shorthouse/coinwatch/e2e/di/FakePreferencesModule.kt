package dev.shorthouse.coinwatch.e2e.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.shorthouse.coinwatch.data.source.local.datastore.favourites.FavouritesPreferences
import dev.shorthouse.coinwatch.data.source.local.datastore.favourites.FavouritesPreferencesSerializer
import dev.shorthouse.coinwatch.data.source.local.datastore.global.UserPreferences
import dev.shorthouse.coinwatch.data.source.local.datastore.global.UserPreferencesSerializer
import dev.shorthouse.coinwatch.data.source.local.datastore.market.MarketPreferences
import dev.shorthouse.coinwatch.data.source.local.datastore.market.MarketPreferencesSerializer
import dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics.ReviewAnalytics
import dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics.ReviewAnalyticsSerializer
import dev.shorthouse.coinwatch.di.PreferencesModule
import java.io.File
import java.util.UUID
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PreferencesModule::class]
)
object FakePreferencesModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext appContext: Context,
    ): DataStore<UserPreferences> {
        val preferencesFile = appContext.testPreferencesFile("user_preferences")

        return DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { preferencesFile },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserPreferences() }
            )
        )
    }

    @Provides
    @Singleton
    fun provideFavouritesPreferencesDataStore(
        @ApplicationContext appContext: Context,
    ): DataStore<FavouritesPreferences> {
        val preferencesFile = appContext.testPreferencesFile("favourites_preferences")

        return DataStoreFactory.create(
            serializer = FavouritesPreferencesSerializer,
            produceFile = { preferencesFile },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { FavouritesPreferences() }
            )
        )
    }

    @Provides
    @Singleton
    fun provideMarketPreferences(
        @ApplicationContext appContext: Context,
    ): DataStore<MarketPreferences> {
        val preferencesFile = appContext.testPreferencesFile("market_preferences")

        return DataStoreFactory.create(
            serializer = MarketPreferencesSerializer,
            produceFile = { preferencesFile },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { MarketPreferences() }
            )
        )
    }

    @Provides
    @Singleton
    fun provideReviewAnalytics(
        @ApplicationContext appContext: Context,
    ): DataStore<ReviewAnalytics> {
        val preferencesFile = appContext.testPreferencesFile("review_analytics")

        return DataStoreFactory.create(
            serializer = ReviewAnalyticsSerializer,
            produceFile = { preferencesFile },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { ReviewAnalytics() }
            )
        )
    }

    private fun Context.testPreferencesFile(name: String): File {
        return File(cacheDir, "${name}_${UUID.randomUUID()}.pb")
    }
}

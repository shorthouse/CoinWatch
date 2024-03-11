package dev.shorthouse.coinwatch.data

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.preferences.favourites.FavouritesPreferencesRepository
import dev.shorthouse.coinwatch.data.preferences.favourites.FavouritesPreferencesSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavouritesPreferencesRepositoryTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher + Job())

    private val testDataStore = DataStoreFactory.create(
        serializer = FavouritesPreferencesSerializer,
        scope = testCoroutineScope,
        produceFile = { testContext.dataStoreFile("test_datastore") }
    )

    // Class under test
    private val favouritesPreferencesRepository = FavouritesPreferencesRepository(testDataStore)

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testCoroutineScope.cancel()
    }

    @Test
    fun when_isFavouritesCondensedTrue_should_updateFavouritesPreferences() =
        testCoroutineScope.runTest {
            val isCondensed = true

            favouritesPreferencesRepository.updateIsFavouritesCondensed(
                isCondensed = isCondensed
            )

            val favouritesPreferences = favouritesPreferencesRepository
                .favouritesPreferencesFlow
                .first()

            assertThat(favouritesPreferences.isFavouritesCondensed).isTrue()
        }

    @Test
    fun when_isFavouritesCondensedFalse_should_updateFavouritesPreferences() =
        testCoroutineScope.runTest {
            val isCondensed = false

            favouritesPreferencesRepository.updateIsFavouritesCondensed(
                isCondensed = isCondensed
            )

            val favouritesPreferences = favouritesPreferencesRepository
                .favouritesPreferencesFlow
                .first()

            assertThat(favouritesPreferences.isFavouritesCondensed).isFalse()
        }
}

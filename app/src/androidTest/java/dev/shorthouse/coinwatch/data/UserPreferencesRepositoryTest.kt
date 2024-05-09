package dev.shorthouse.coinwatch.data

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.data.preferences.global.StartScreen
import dev.shorthouse.coinwatch.data.preferences.global.UserPreferencesRepository
import dev.shorthouse.coinwatch.data.preferences.global.UserPreferencesSerializer
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
class UserPreferencesRepositoryTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher + Job())

    private val testDataStore = DataStoreFactory.create(
        serializer = UserPreferencesSerializer,
        scope = testCoroutineScope,
        produceFile = { testContext.dataStoreFile("test_datastore") }
    )

    // Class under test
    private val userPreferencesRepository = UserPreferencesRepository(testDataStore)

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
    fun when_setCurrencyToUSD_should_updateUserPreferencesCurrencyToUSD() =
        testCoroutineScope.runTest {
            val currency = Currency.USD

            userPreferencesRepository.updateCurrency(
                currency = currency
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences.currency).isEqualTo(currency)
        }

    @Test
    fun when_setCurrencyToGBP_should_updateUserPreferencesCurrencyToGBP() =
        testCoroutineScope.runTest {
            val currency = Currency.GBP

            userPreferencesRepository.updateCurrency(
                currency = currency
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences.currency).isEqualTo(currency)
        }

    @Test
    fun when_setCurrencyToEUR_should_updateUserPreferencesCurrencyToEUR() =
        testCoroutineScope.runTest {
            val currency = Currency.EUR

            userPreferencesRepository.updateCurrency(
                currency = currency
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences.currency).isEqualTo(currency)
        }

    @Test
    fun when_setStartScreenToMarket_should_updateUserPreferencesStartScreenToMarket() =
        testCoroutineScope.runTest {
            val startScreen = StartScreen.Market

            userPreferencesRepository.updateStartScreen(
                startScreen = startScreen
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences.startScreen).isEqualTo(startScreen)
        }

    @Test
    fun when_setStartScreenToFavourites_should_updateUserPreferencesStartScreenToFavourites() =
        testCoroutineScope.runTest {
            val startScreen = StartScreen.Favourites

            userPreferencesRepository.updateStartScreen(
                startScreen = startScreen
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences.startScreen).isEqualTo(startScreen)
        }

    @Test
    fun when_setStartScreenToSearch_should_updateUserPreferencesStartScreenToSearch() =
        testCoroutineScope.runTest {
            val startScreen = StartScreen.Search

            userPreferencesRepository.updateStartScreen(
                startScreen = startScreen
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences.startScreen).isEqualTo(startScreen)
        }
}

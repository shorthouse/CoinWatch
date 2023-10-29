package dev.shorthouse.coinwatch.data

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.datastore.Currency
import dev.shorthouse.coinwatch.data.datastore.UserPreferences
import dev.shorthouse.coinwatch.data.datastore.UserPreferencesRepository
import dev.shorthouse.coinwatch.data.datastore.UserPreferencesSerializer
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
            val expectedUserPreferences = UserPreferences(
                currency = Currency.USD
            )

            userPreferencesRepository.updateCurrency(
                currency = Currency.USD
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences).isEqualTo(expectedUserPreferences)
        }

    @Test
    fun when_setCurrencyToGBP_should_updateUserPreferencesCurrencyToGBP() =
        testCoroutineScope.runTest {
            val expectedUserPreferences = UserPreferences(
                currency = Currency.GBP
            )

            userPreferencesRepository.updateCurrency(
                currency = Currency.GBP
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences).isEqualTo(expectedUserPreferences)
        }

    @Test
    fun when_setCurrencyToEUR_should_updateUserPreferencesCurrencyToEUR() =
        testCoroutineScope.runTest {
            val expectedUserPreferences = UserPreferences(
                currency = Currency.EUR
            )

            userPreferencesRepository.updateCurrency(
                currency = Currency.EUR
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences).isEqualTo(expectedUserPreferences)
        }
}

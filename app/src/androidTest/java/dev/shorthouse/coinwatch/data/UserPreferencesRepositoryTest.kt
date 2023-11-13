package dev.shorthouse.coinwatch.data

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.data.datastore.Currency
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
    fun when_setCoinSortToMarketCap_should_updateUserPreferencesCoinSortToMarketCap() =
        testCoroutineScope.runTest {
            val coinSort = CoinSort.MarketCap

            userPreferencesRepository.updateCoinSort(
                coinSort = coinSort
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences.coinSort).isEqualTo(coinSort)
        }

    @Test
    fun when_setCoinSortToPrice_should_updateUserPreferencesCoinSortToPrice() =
        testCoroutineScope.runTest {
            val coinSort = CoinSort.Price

            userPreferencesRepository.updateCoinSort(
                coinSort = coinSort
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences.coinSort).isEqualTo(coinSort)
        }

    @Test
    fun when_setCoinSortToPriceChange_should_updateUserPreferencesCoinSortToPriceChange() =
        testCoroutineScope.runTest {
            val coinSort = CoinSort.PriceChange24h

            userPreferencesRepository.updateCoinSort(
                coinSort = coinSort
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences.coinSort).isEqualTo(coinSort)
        }

    @Test
    fun when_setCoinSortToVolume_should_updateUserPreferencesCoinSortToVolume() =
        testCoroutineScope.runTest {
            val coinSort = CoinSort.Volume24h

            userPreferencesRepository.updateCoinSort(
                coinSort = coinSort
            )

            val userPreferences = userPreferencesRepository
                .userPreferencesFlow
                .first()

            assertThat(userPreferences.coinSort).isEqualTo(coinSort)
        }
}

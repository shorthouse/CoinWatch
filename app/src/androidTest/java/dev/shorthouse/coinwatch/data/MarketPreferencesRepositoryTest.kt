package dev.shorthouse.coinwatch.data

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.preferences.market.MarketPreferencesRepository
import dev.shorthouse.coinwatch.data.source.local.preferences.market.MarketPreferencesSerializer
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
class MarketPreferencesRepositoryTest {

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher + Job())

    private val testDataStore = DataStoreFactory.create(
        serializer = MarketPreferencesSerializer,
        scope = testCoroutineScope,
        produceFile = { testContext.dataStoreFile("test_datastore") }
    )

    // Class under test
    private val marketPreferencesRepository = MarketPreferencesRepository(testDataStore)

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
    fun when_coinSortMarketCap_should_updateMarketPreferences() = testCoroutineScope.runTest {
        val coinSort = CoinSort.MarketCap

        marketPreferencesRepository.updateCoinSort(coinSort = coinSort)

        val marketPreferences = marketPreferencesRepository
            .marketPreferencesFlow
            .first()

        assertThat(marketPreferences.coinSort).isEqualTo(coinSort)
    }

    @Test
    fun when_coinSortPopular_should_updateMarketPreferences() = testCoroutineScope.runTest {
        val coinSort = CoinSort.Popular

        marketPreferencesRepository.updateCoinSort(coinSort = coinSort)

        val marketPreferences = marketPreferencesRepository
            .marketPreferencesFlow
            .first()

        assertThat(marketPreferences.coinSort).isEqualTo(coinSort)
    }

    @Test
    fun when_coinSortGainers_should_updateMarketPreferences() = testCoroutineScope.runTest {
        val coinSort = CoinSort.Gainers

        marketPreferencesRepository.updateCoinSort(coinSort = coinSort)

        val marketPreferences = marketPreferencesRepository
            .marketPreferencesFlow
            .first()

        assertThat(marketPreferences.coinSort).isEqualTo(coinSort)
    }

    @Test
    fun when_coinSortLosers_should_updateMarketPreferences() = testCoroutineScope.runTest {
        val coinSort = CoinSort.Losers

        marketPreferencesRepository.updateCoinSort(coinSort = coinSort)

        val marketPreferences = marketPreferencesRepository
            .marketPreferencesFlow
            .first()
    }

    @Test
    fun when_coinSortNewest_should_updateMarketPreferences() = testCoroutineScope.runTest {
        val coinSort = CoinSort.Newest

        marketPreferencesRepository.updateCoinSort(coinSort = coinSort)

        val marketPreferences = marketPreferencesRepository
            .marketPreferencesFlow
            .first()

        assertThat(marketPreferences.coinSort).isEqualTo(coinSort)
    }
}

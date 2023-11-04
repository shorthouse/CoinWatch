package dev.shorthouse.coinwatch.ui.screen.market

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.data.datastore.Currency
import dev.shorthouse.coinwatch.data.datastore.UserPreferences
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.domain.GetCachedCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.RefreshCachedCoinsUseCase
import dev.shorthouse.coinwatch.domain.UpdateCoinSortUseCase
import dev.shorthouse.coinwatch.domain.UpdateCurrencyUseCase
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MarketViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var viewModel: MarketViewModel

    @RelaxedMockK
    private lateinit var getCachedCoinsUseCase: GetCachedCoinsUseCase

    @RelaxedMockK
    private lateinit var refreshCachedCoinsUseCase: RefreshCachedCoinsUseCase

    @RelaxedMockK
    private lateinit var getUserPreferencesUseCase: GetUserPreferencesUseCase

    @RelaxedMockK
    private lateinit var updateCoinSortUseCase: UpdateCoinSortUseCase

    @RelaxedMockK
    private lateinit var updateCurrencyUseCase: UpdateCurrencyUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = MarketViewModel(
            getCachedCoinsUseCase = getCachedCoinsUseCase,
            refreshCachedCoinsUseCase = refreshCachedCoinsUseCase,
            getUserPreferencesUseCase = getUserPreferencesUseCase,
            updateCoinSortUseCase = updateCoinSortUseCase,
            updateCurrencyUseCase = updateCurrencyUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When ViewModel is initialised should have loading UI state`() = runTest {
        // Arrange
        val expectedUiState = MarketUiState(isLoading = true)

        // Act

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When coins returns error should have error UI state`() = runTest {
        // Arrange
        val errorMessage = "Coins error"
        val expectedUiState = MarketUiState(errorMessage = errorMessage)

        val userPreferences = UserPreferences(
            coinSort = CoinSort.MarketCap,
            currency = Currency.USD
        )

        every { getCachedCoinsUseCase() } returns flowOf(Result.Error(errorMessage))
        every { getUserPreferencesUseCase() } returns flowOf(userPreferences)

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When coins and user prefs return success should have success UI state`() = runTest {
        // Arrange
        val cachedCoins = persistentListOf(
            CachedCoin(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "",
                currentPrice = Price("200.0"),
                priceChangePercentage24h = Percentage("1.0"),
                prices24h = persistentListOf()
            ),
            CachedCoin(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "",
                currentPrice = Price("100.0"),
                priceChangePercentage24h = Percentage("2.0"),
                prices24h = persistentListOf()
            )
        )

        val userPreferences = UserPreferences(
            coinSort = CoinSort.MarketCap,
            currency = Currency.USD
        )

        val expectedUiState = MarketUiState(
            coins = cachedCoins,
            coinSort = CoinSort.MarketCap,
            showCoinSortBottomSheet = false,
            coinCurrency = Currency.USD,
            showCoinCurrencyBottomSheet = false
        )

        every { getCachedCoinsUseCase() } returns flowOf(Result.Success(cachedCoins))
        every { getUserPreferencesUseCase() } returns flowOf(userPreferences)

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }
}

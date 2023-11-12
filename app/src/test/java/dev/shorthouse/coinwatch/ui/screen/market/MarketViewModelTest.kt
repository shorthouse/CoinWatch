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
import io.mockk.coEvery
import io.mockk.coVerify
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
    fun `When cached coins returns success should update UI state with coins`() {
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

        val expectedUiState = MarketUiState(
            coins = cachedCoins,
            isLoading = false,
            errorMessage = null
        )

        every { getCachedCoinsUseCase() } returns flowOf(Result.Success(cachedCoins))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When cached coins returns error should update UI state with error message`() {
        // Arrange
        val errorMessage = "Coins error"
        val expectedUiState = MarketUiState(
            isLoading = false,
            errorMessage = errorMessage
        )

        every { getCachedCoinsUseCase() } returns flowOf(Result.Error(errorMessage))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When user preferences initialises should update UI state and refresh cached coins`() {
        // Arrange
        val coinSort = CoinSort.Price
        val currency = Currency.GBP

        val userPreferences = UserPreferences(
            coinSort = coinSort,
            currency = currency
        )

        val expectedUiState = MarketUiState(
            coinSort = coinSort,
            currency = currency,
            isLoading = true
        )

        every { getUserPreferencesUseCase() } returns flowOf(userPreferences)
        coEvery {
            refreshCachedCoinsUseCase(
                coinSort = coinSort,
                currency = currency
            )
        } returns Result.Success(emptyList())

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
        coVerify {
            getUserPreferencesUseCase()
            refreshCachedCoinsUseCase(
                coinSort = coinSort,
                currency = currency
            )
        }
    }

    @Test
    fun `When coin sort updates should call use case`() {
        // Arrange
        val coinSort = CoinSort.Price

        // Act
        viewModel.updateCoinSort(coinSort)

        // Assert
        coVerify {
            updateCoinSortUseCase(coinSort)
        }
    }

    @Test
    fun `When currency updates should call use case`() {
        // Arrange
        val currency = Currency.GBP

        // Act
        viewModel.updateCurrency(currency)

        // Assert
        coVerify {
            updateCurrencyUseCase(currency)
        }
    }

    @Test
    fun `When update show coin sort bottom sheet updates called update UI state`() {
        // Arrange
        val currentShowSheet = viewModel.uiState.value.showCoinSortBottomSheet
        val newShowSheet = currentShowSheet.not()

        // Act
        viewModel.updateShowCoinSortBottomSheet(newShowSheet)

        // Assert
        assertThat(viewModel.uiState.value.showCoinSortBottomSheet).isEqualTo(newShowSheet)
    }

    @Test
    fun `When update show coin currency bottom sheet called should update UI state`() {
        // Arrange
        val currentShowSheet = viewModel.uiState.value.showCurrencyBottomSheet
        val newShowSheet = currentShowSheet.not()

        // Act
        viewModel.onUpdateShowCurrencyBottomSheet(newShowSheet)

        // Assert
        assertThat(viewModel.uiState.value.showCurrencyBottomSheet).isEqualTo(newShowSheet)
    }

    @Test
    fun `When pull refresh cached coins called should refresh cached coins with user prefs`() {
        // Arrange
        val coinSort = CoinSort.Price
        val currency = Currency.GBP

        val userPreferences = UserPreferences(
            coinSort = coinSort,
            currency = currency
        )

        every { getUserPreferencesUseCase() } returns flowOf(userPreferences)
        coEvery {
            refreshCachedCoinsUseCase(
                coinSort = coinSort,
                currency = currency
            )
        } returns Result.Success(emptyList())

        // Act
        viewModel.pullRefreshCachedCoins()

        // Assert
        coVerify {
            getUserPreferencesUseCase()
            refreshCachedCoinsUseCase(
                coinSort = coinSort,
                currency = currency
            )
        }
    }
}

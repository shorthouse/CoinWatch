package dev.shorthouse.coinwatch.ui.screen.market

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.data.userPreferences.UserPreferences
import dev.shorthouse.coinwatch.domain.GetCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetMarketStatsUseCase
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.UpdateCachedCoinsUseCase
import dev.shorthouse.coinwatch.domain.UpdateCoinSortUseCase
import dev.shorthouse.coinwatch.domain.UpdateCurrencyUseCase
import dev.shorthouse.coinwatch.model.MarketStats
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
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
    private lateinit var getCoinsUseCase: GetCoinsUseCase

    @RelaxedMockK
    private lateinit var getMarketStatsUseCase: GetMarketStatsUseCase

    @RelaxedMockK
    private lateinit var updateCachedCoinsUseCase: UpdateCachedCoinsUseCase

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
            getCoinsUseCase = getCoinsUseCase,
            getMarketStatsUseCase = getMarketStatsUseCase,
            updateCachedCoinsUseCase = updateCachedCoinsUseCase,
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
    fun `When ViewModel is initialised should have loading UI state`() {
        // Arrange

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value.isLoading).isTrue()
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

        every { getCoinsUseCase() } returns flowOf(Result.Success(cachedCoins))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value.coins).isEqualTo(cachedCoins)
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `When cached coins returns error should update UI state with error message`() {
        // Arrange
        every { getCoinsUseCase() } returns flowOf(Result.Error("Coins error"))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value.isLoading).isFalse()
        assertThat(viewModel.uiState.value.errorMessageIds).contains(R.string.error_local_coins)
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

        every { getUserPreferencesUseCase() } returns flowOf(userPreferences)
        coEvery {
            updateCachedCoinsUseCase(
                coinSort = coinSort,
                currency = currency
            )
        } returns Result.Success(emptyList())

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value.coinSort).isEqualTo(coinSort)
        assertThat(viewModel.uiState.value.currency).isEqualTo(currency)
        coVerify {
            getUserPreferencesUseCase()
            updateCachedCoinsUseCase(
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
        val currentShowSheet = viewModel.uiState.value.isCoinSortSheetShown
        val newShowSheet = currentShowSheet.not()

        // Act
        viewModel.updateIsCoinSortSheetShown(newShowSheet)

        // Assert
        assertThat(viewModel.uiState.value.isCoinSortSheetShown).isEqualTo(newShowSheet)
    }

    @Test
    fun `When showing coin sort sheet with another sheet already open should not show sheet`() {
        // Arrange

        // Act
        viewModel.updateIsCurrencySheetShown(true)
        viewModel.updateIsCoinSortSheetShown(true)

        // Assert
        assertThat(viewModel.uiState.value.isCoinSortSheetShown).isFalse()
    }

    @Test
    fun `When update show coin currency bottom sheet called should update UI state`() {
        // Arrange
        val currentShowSheet = viewModel.uiState.value.isCurrencySheetShown
        val newShowSheet = currentShowSheet.not()

        // Act
        viewModel.updateIsCurrencySheetShown(newShowSheet)

        // Assert
        assertThat(viewModel.uiState.value.isCurrencySheetShown).isEqualTo(newShowSheet)
    }

    @Test
    fun `When showing currency sheet with another sheet already open should not show sheet`() {
        // Arrange

        // Act
        viewModel.updateIsCoinSortSheetShown(true)
        viewModel.updateIsCurrencySheetShown(true)

        // Assert
        assertThat(viewModel.uiState.value.isCurrencySheetShown).isFalse()
    }

    @Test
    fun `When pull refresh cached coins called should refresh cached coins with user prefs`() {
        // Arrange
        every { getUserPreferencesUseCase() } returns flowOf(UserPreferences())
        coEvery { updateCachedCoinsUseCase(any(), any()) } returns Result.Success(emptyList())

        // Act
        viewModel.pullRefreshCachedCoins()

        // Assert
        assertThat(viewModel.uiState.value.isRefreshing).isTrue()
    }

    @Test
    fun `When dismissing error should remove specified error from list`() {
        // Arrange
        every { getCoinsUseCase() } returns flowOf(Result.Error("Coins error"))

        // Act
        viewModel.initialiseUiState()

        val errorIsInserted = viewModel.uiState.value.errorMessageIds.isNotEmpty()
        val errorMessageId = viewModel.uiState.value.errorMessageIds.first()
        viewModel.dismissErrorMessage(errorMessageId)

        // Assert
        assertThat(errorIsInserted).isTrue()
        assertThat(viewModel.uiState.value.errorMessageIds).doesNotContain(errorMessageId)
    }

    @Test
    fun `When morning hour should return morning time of day`() {
        // Arrange
        val expectedTimeOfDay = TimeOfDay.Morning
        val morningHours = 4..11

        // Act
        val timeOfDays = morningHours.map { morningHour ->
            viewModel.calculateTimeOfDay(morningHour)
        }

        // Assert
        timeOfDays.forEach {
            assertThat(it).isEqualTo(expectedTimeOfDay)
        }
    }

    @Test
    fun `When afternoon hour should return afternoon time of day`() {
        // Arrange
        val expectedTimeOfDay = TimeOfDay.Afternoon
        val afternoonHours = 12..17

        // Act
        val timeOfDays = afternoonHours.map { afternoonHour ->
            viewModel.calculateTimeOfDay(afternoonHour)
        }

        // Assert
        timeOfDays.forEach {
            assertThat(it).isEqualTo(expectedTimeOfDay)
        }
    }

    @Test
    fun `When evening hour should return evening time of day`() {
        // Arrange
        val expectedTimeOfDay = TimeOfDay.Evening
        val eveningHoursPartOne = 18..23
        val eveningHoursPartTwo = 0..3

        // Act
        val timeOfDays = eveningHoursPartOne.map { eveningHour ->
            viewModel.calculateTimeOfDay(eveningHour)
        } + eveningHoursPartTwo.map { eveningHour ->
            viewModel.calculateTimeOfDay(eveningHour)
        }

        // Assert
        timeOfDays.forEach {
            assertThat(it).isEqualTo(expectedTimeOfDay)
        }
    }

    @Test
    fun `Market cap change percentage 24h should have default null value before initialising`() {
        // Arrange

        // Act

        // Assert
        assertThat(viewModel.uiState.value.marketCapChangePercentage24h).isNull()
    }

    @Test
    fun `When market stats returns success should set market cap change percentage 24h`() {
        // Arrange
        val expectedMarketCapChangePercentage24h = Percentage("2.0")
        val marketStats = MarketStats(marketCapChangePercentage24h = Percentage("2.0"))

        coEvery { getMarketStatsUseCase() } returns Result.Success(marketStats)

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value.marketCapChangePercentage24h)
            .isEqualTo(expectedMarketCapChangePercentage24h)
    }

    @Test
    fun `When market stats returns error should create error message and not populate value`() {
        // Arrange
        coEvery { getMarketStatsUseCase() } returns Result.Error("Market stats error")

        // Act

        // Assert
        assertThat(viewModel.uiState.value.errorMessageIds).contains(R.string.error_market_stats)
        assertThat(viewModel.uiState.value.marketCapChangePercentage24h).isNull()
    }
}

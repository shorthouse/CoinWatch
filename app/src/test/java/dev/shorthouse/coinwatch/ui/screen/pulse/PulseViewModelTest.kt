package dev.shorthouse.coinwatch.ui.screen.pulse

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.local.datastore.global.StartScreen
import dev.shorthouse.coinwatch.data.source.local.datastore.global.UserPreferences
import dev.shorthouse.coinwatch.domain.preferences.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.pulse.GetFearGreedUseCase
import dev.shorthouse.coinwatch.domain.pulse.GetGlobalMarketUseCase
import dev.shorthouse.coinwatch.domain.pulse.GetMoversUseCase
import dev.shorthouse.coinwatch.domain.pulse.GetTrendingCoinsUseCase
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.MoverCoin
import dev.shorthouse.coinwatch.model.Movers
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.TrendingCoin
import dev.shorthouse.coinwatch.rule.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import java.math.BigDecimal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PulseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PulseViewModel

    @MockK
    private lateinit var getFearGreedUseCase: GetFearGreedUseCase

    @MockK
    private lateinit var getGlobalMarketUseCase: GetGlobalMarketUseCase

    @MockK
    private lateinit var getTrendingCoinsUseCase: GetTrendingCoinsUseCase

    @MockK
    private lateinit var getMoversUseCase: GetMoversUseCase

    @MockK
    private lateinit var getUserPreferencesUseCase: GetUserPreferencesUseCase

    private lateinit var userPreferencesFlow: MutableStateFlow<UserPreferences>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When initial fear greed returns success should update UI state`() {
        val fearGreed = fearGreed()

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            globalMarket()
        )

        initialiseViewModel()

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = fearGreed,
                globalMarket = globalMarket(),
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers
            )
        )
    }

    @Test
    fun `When initial fear greed returns error should update UI state with error`() {
        coEvery { getFearGreedUseCase() } returns Result.Error("Unable to fetch fear and greed")
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            globalMarket()
        )

        initialiseViewModel()

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                globalMarket = globalMarket(),
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers,
                errorMessageIds = setOf(R.string.error_pulse_market_mood)
            )
        )
    }

    @Test
    fun `When initial global market returns success should update UI state`() {
        val fearGreed = fearGreed()
        val globalMarket = globalMarket(currency = Currency.GBP)

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.GBP) } returns Result.Success(
            globalMarket
        )

        initialiseViewModel(
            currency = Currency.GBP
        )

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = fearGreed,
                globalMarket = globalMarket,
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers
            )
        )
    }

    @Test
    fun `When initial global market returns error should update UI state with error`() {
        val fearGreed = fearGreed()

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Error(
            "Unable to fetch global market"
        )

        initialiseViewModel()

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = fearGreed,
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers,
                errorMessageIds = setOf(R.string.error_pulse_global_market)
            )
        )
    }

    @Test
    fun `When initial global market returns success should use selected currency`() {
        val fearGreed = fearGreed()
        val gbpGlobalMarket = globalMarket(currency = Currency.GBP)

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.GBP) } returns Result.Success(
            gbpGlobalMarket
        )

        initialiseViewModel(
            currency = Currency.GBP
        )

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = fearGreed,
                globalMarket = gbpGlobalMarket,
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers
            )
        )
        coVerify(exactly = 1) {
            getGlobalMarketUseCase(currency = Currency.GBP)
        }
    }

    @Test
    fun `When currency changes should reload global market`() {
        val fearGreed = fearGreed()
        val initialGlobalMarket = globalMarket(currency = Currency.USD)
        val updatedGlobalMarket = globalMarket(currency = Currency.GBP)

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            initialGlobalMarket
        )
        coEvery { getGlobalMarketUseCase(currency = Currency.GBP) } returns Result.Success(
            updatedGlobalMarket
        )

        initialiseViewModel()
        userPreferencesFlow.value = UserPreferences(currency = Currency.GBP)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = fearGreed,
                globalMarket = updatedGlobalMarket,
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers
            )
        )
        coVerify(exactly = 1) {
            getGlobalMarketUseCase(currency = Currency.USD)
        }
        coVerify(exactly = 1) {
            getGlobalMarketUseCase(currency = Currency.GBP)
        }
    }

    @Test
    fun `When non-currency preferences change should not reload global market`() {
        val fearGreed = fearGreed()
        val initialGlobalMarket = globalMarket(currency = Currency.USD)

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            initialGlobalMarket
        )

        initialiseViewModel()
        userPreferencesFlow.value = UserPreferences(
            currency = Currency.USD,
            startScreen = StartScreen.Pulse
        )
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = fearGreed,
                globalMarket = initialGlobalMarket,
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers
            )
        )
        coVerify(exactly = 1) {
            getGlobalMarketUseCase(currency = Currency.USD)
        }
    }

    @Test
    fun `When refreshing succeeds should update fear greed`() {
        val initialFearGreed = fearGreed(value = 42)
        val refreshedFearGreed = fearGreed(value = 60)

        coEvery { getFearGreedUseCase() } returnsMany listOf(
            Result.Success(initialFearGreed),
            Result.Success(refreshedFearGreed)
        )
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            globalMarket()
        )

        initialiseViewModel()
        viewModel.refreshUiState()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = refreshedFearGreed,
                globalMarket = globalMarket(),
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers
            )
        )
        coVerify(exactly = 2) {
            getFearGreedUseCase()
        }
        coVerify(exactly = 2) {
            getGlobalMarketUseCase(currency = Currency.USD)
        }
    }

    @Test
    fun `When refreshing errors should keep existing fear greed and add error`() {
        val initialFearGreed = fearGreed()

        coEvery { getFearGreedUseCase() } returnsMany listOf(
            Result.Success(initialFearGreed),
            Result.Error("Unable to fetch fear and greed")
        )
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            globalMarket()
        )

        initialiseViewModel()
        viewModel.refreshUiState()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = initialFearGreed,
                globalMarket = globalMarket(),
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers,
                errorMessageIds = setOf(R.string.error_pulse_market_mood)
            )
        )
    }

    @Test
    fun `When refreshing after initial error succeeds should keep existing error message`() {
        val refreshedFearGreed = fearGreed()

        coEvery { getFearGreedUseCase() } returnsMany listOf(
            Result.Error("Unable to fetch fear and greed"),
            Result.Success(refreshedFearGreed)
        )
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            globalMarket()
        )

        initialiseViewModel()
        viewModel.refreshUiState()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = refreshedFearGreed,
                globalMarket = globalMarket(),
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers,
                errorMessageIds = setOf(R.string.error_pulse_market_mood)
            )
        )
    }

    @Test
    fun `When global market succeeds after initial error should keep existing error message`() {
        val fearGreed = fearGreed()
        val globalMarket = globalMarket()

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returnsMany listOf(
            Result.Error("Unable to fetch global market"),
            Result.Success(globalMarket)
        )

        initialiseViewModel()
        viewModel.refreshUiState()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = fearGreed,
                globalMarket = globalMarket,
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers,
                errorMessageIds = setOf(R.string.error_pulse_global_market)
            )
        )
    }

    @Test
    fun `When refreshing global market errors should keep existing global market and add error`() {
        val fearGreed = fearGreed()
        val initialGlobalMarket = globalMarket()

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returnsMany listOf(
            Result.Success(initialGlobalMarket),
            Result.Error("Unable to fetch global market")
        )

        initialiseViewModel()
        viewModel.refreshUiState()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = fearGreed,
                globalMarket = initialGlobalMarket,
                trendingCoins = expectedTrendingCoins,
                movers = expectedMovers,
                errorMessageIds = setOf(R.string.error_pulse_global_market)
            )
        )
    }

    @Test
    fun `When dismissing error should remove specified error from list`() {
        coEvery { getFearGreedUseCase() } returns Result.Error("Unable to fetch fear and greed")
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            globalMarket()
        )

        initialiseViewModel()
        viewModel.dismissErrorMessage(R.string.error_pulse_market_mood)

        assertThat(viewModel.uiState.value.errorMessageIds).isEmpty()
    }

    @Test
    fun `When initial trending coins returns success should update UI state`() {
        val fearGreed = fearGreed()

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            globalMarket()
        )

        initialiseViewModel()

        assertThat(viewModel.uiState.value.trendingCoins).isEqualTo(expectedTrendingCoins)
    }

    @Test
    fun `When initial trending coins returns error should update UI state with error`() {
        val fearGreed = fearGreed()

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            globalMarket()
        )

        initialiseViewModel(
            trendingResult = Result.Error("Unable to fetch trending coins")
        )

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = fearGreed,
                globalMarket = globalMarket(),
                trendingCoins = persistentListOf(),
                movers = expectedMovers,
                errorMessageIds = setOf(R.string.error_pulse_trending)
            )
        )
    }

    @Test
    fun `When currency changes should reload trending coins`() {
        val fearGreed = fearGreed()

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = any()) } returns Result.Success(globalMarket())

        initialiseViewModel()
        userPreferencesFlow.value = UserPreferences(currency = Currency.GBP)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) {
            getTrendingCoinsUseCase(currency = Currency.USD)
        }
        coVerify(exactly = 1) {
            getTrendingCoinsUseCase(currency = Currency.GBP)
        }
    }

    @Test
    fun `When initial movers returns success should update UI state`() {
        val fearGreed = fearGreed()

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            globalMarket()
        )

        initialiseViewModel()

        assertThat(viewModel.uiState.value.movers).isEqualTo(expectedMovers)
    }

    @Test
    fun `When initial movers returns error should update UI state with error`() {
        val fearGreed = fearGreed()

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = Currency.USD) } returns Result.Success(
            globalMarket()
        )

        initialiseViewModel(
            moversResult = Result.Error("Unable to fetch movers")
        )

        assertThat(viewModel.uiState.value).isEqualTo(
            PulseUiState(
                fearGreed = fearGreed,
                globalMarket = globalMarket(),
                trendingCoins = expectedTrendingCoins,
                movers = null,
                errorMessageIds = setOf(R.string.error_pulse_movers)
            )
        )
    }

    @Test
    fun `When currency changes should reload movers`() {
        val fearGreed = fearGreed()

        coEvery { getFearGreedUseCase() } returns Result.Success(fearGreed)
        coEvery { getGlobalMarketUseCase(currency = any()) } returns Result.Success(globalMarket())

        initialiseViewModel()
        userPreferencesFlow.value = UserPreferences(currency = Currency.GBP)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) {
            getMoversUseCase(currency = Currency.USD)
        }
        coVerify(exactly = 1) {
            getMoversUseCase(currency = Currency.GBP)
        }
    }

    private fun initialiseViewModel(
        currency: Currency = Currency.USD,
        trendingResult: Result<List<TrendingCoin>> = Result.Success(trendingCoinsList()),
        moversResult: Result<Movers> = Result.Success(movers()),
    ) {
        userPreferencesFlow = MutableStateFlow(UserPreferences(currency = currency))
        every { getUserPreferencesUseCase() } returns userPreferencesFlow
        coEvery { getTrendingCoinsUseCase(currency = any()) } returns trendingResult
        coEvery { getMoversUseCase(currency = any()) } returns moversResult

        viewModel = PulseViewModel(
            getFearGreedUseCase = getFearGreedUseCase,
            getGlobalMarketUseCase = getGlobalMarketUseCase,
            getTrendingCoinsUseCase = getTrendingCoinsUseCase,
            getMoversUseCase = getMoversUseCase,
            getUserPreferencesUseCase = getUserPreferencesUseCase
        )
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
    }

    private fun fearGreed(
        value: Int = 42,
        moodBand: FearGreedMoodBand = FearGreedMoodBand.Fear,
    ): FearGreed =
        FearGreed(
            value = value,
            moodBand = moodBand,
            history = persistentListOf(
                BigDecimal("35"),
                BigDecimal("40"),
                BigDecimal(value.toString())
            )
        )

    private fun globalMarket(currency: Currency = Currency.USD): GlobalMarket =
        GlobalMarket(
            totalMarketCap = Price("2410000000000", currency),
            volume24h = Price("98200000000", currency),
            btcDominancePercentage = BigDecimal("54.2"),
            coinsUp24h = 2841,
            coinsDown24h = 1893
        )

    private fun trendingCoinsList(): List<TrendingCoin> =
        listOf(
            TrendingCoin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29446.336548759988", Currency.USD),
                priceChangePercentage24h = Percentage("1.76833"),
                sparkline = persistentListOf(
                    BigDecimal("29100"),
                    BigDecimal("29250"),
                    BigDecimal("29446")
                )
            )
        )

    private fun movers(): Movers =
        Movers(
            topGainer = MoverCoin(
                id = "gainer",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29446.336548759988", Currency.USD),
                priceChangePercentage24h = Percentage("12.5"),
                sparkline = persistentListOf()
            ),
            topLoser = MoverCoin(
                id = "loser",
                name = "Dogecoin",
                symbol = "DOGE",
                imageUrl = "https://cdn.coinranking.com/H1arVnjQ_/doge.svg",
                currentPrice = Price("0.11923", Currency.USD),
                priceChangePercentage24h = Percentage("-9.3"),
                sparkline = persistentListOf()
            ),
            mostMovement = persistentListOf()
        )

    private val expectedTrendingCoins: ImmutableList<TrendingCoin>
        get() = trendingCoinsList().toPersistentList()

    private val expectedMovers: Movers
        get() = movers()
}

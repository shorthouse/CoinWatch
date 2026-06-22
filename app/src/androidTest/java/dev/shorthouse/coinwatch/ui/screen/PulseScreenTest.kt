package dev.shorthouse.coinwatch.ui.screen

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.TrendingCoin
import dev.shorthouse.coinwatch.ui.screen.pulse.PulseScreen
import dev.shorthouse.coinwatch.ui.screen.pulse.PulseUiState
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.junit.Rule
import org.junit.Test

class PulseScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_uiStateLoading_should_showLoadingIndicator() {
        composeTestRule.setContent {
            AppTheme {
                PulseScreen(
                    uiState = PulseUiState(isLoading = true),
                    onCoinClick = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.onNode(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo)
        ).assertIsDisplayed()
    }

    @Test
    fun when_uiStateLoadingWithExistingPartialContent_should_showLoadingIndicator() {
        composeTestRule.setContent {
            AppTheme {
                PulseScreen(
                    uiState = PulseUiState(
                        fearGreed = fearGreed(),
                        isLoading = true
                    ),
                    onCoinClick = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNode(
                SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo)
            ).assertIsDisplayed()
            onNodeWithText("Market Mood").assertDoesNotExist()
        }
    }

    @Test
    fun when_uiStateErrorWithoutContent_should_showErrorState() {
        composeTestRule.setContent {
            AppTheme {
                PulseScreen(
                    uiState = PulseUiState(
                        errorMessageIds = setOf(R.string.error_pulse_market_mood)
                    ),
                    onCoinClick = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("An error has occurred").assertIsDisplayed()
            onNodeWithText("Pulse data unavailable").assertIsDisplayed()
        }
    }

    @Test
    fun when_uiStateHasOnlyGlobalMarketContent_should_showGlobalMarketContent() {
        val globalMarket = globalMarket()

        composeTestRule.setContent {
            AppTheme {
                PulseScreen(
                    uiState = PulseUiState(
                        globalMarket = globalMarket,
                        errorMessageIds = setOf(R.string.error_pulse_market_mood)
                    ),
                    onCoinClick = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Global Market").assertIsDisplayed()
            onNodeWithText(globalMarket.totalMarketCap.formattedAmount).assertIsDisplayed()
            onNodeWithText("Market mood unavailable").assertIsDisplayed()
        }
    }

    @Test
    fun when_uiStateSuccess_should_showPulseMarketMoodContentAndNoTimestamp() {
        val globalMarket = globalMarket()

        composeTestRule.setContent {
            AppTheme {
                PulseScreen(
                    uiState = PulseUiState(
                        fearGreed = fearGreed(),
                        globalMarket = globalMarket
                    ),
                    onCoinClick = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Pulse").assertIsDisplayed()
            onNodeWithText("Market Mood").assertIsDisplayed()
            onNodeWithText("42").assertIsDisplayed()
            onNodeWithText("Fear").assertIsDisplayed()
            onNodeWithText("out of 100").assertIsDisplayed()
            onNodeWithText("Past 30 days").assertIsDisplayed()
            onNodeWithText("Global Market").performScrollTo().assertIsDisplayed()
            onNodeWithText(globalMarket.totalMarketCap.formattedAmount).assertIsDisplayed()
            onNodeWithText("24h breadth").assertIsDisplayed()
            onNodeWithText("Updated just now").assertDoesNotExist()
        }
    }

    @Test
    fun when_pullRefreshingScreen_should_callOnRefresh() {
        var onRefreshCalled = false

        composeTestRule.setContent {
            AppTheme {
                PulseScreen(
                    uiState = PulseUiState(fearGreed = fearGreed()),
                    onCoinClick = {},
                    onRefresh = { onRefreshCalled = true },
                    onDismissError = {}
                )
            }
        }

        // Start the swipe below the top app bar so the gesture lands on the
        // refreshable content rather than on the (non-scrolling) title bar.
        composeTestRule.onRoot()
            .performTouchInput {
                swipeDown(startY = centerY, endY = bottom)
            }

        assertThat(onRefreshCalled).isTrue()
    }

    @Test
    fun when_errorWithExistingContent_should_showSnackbar() {
        composeTestRule.setContent {
            AppTheme {
                PulseScreen(
                    uiState = PulseUiState(
                        fearGreed = fearGreed(),
                        errorMessageIds = setOf(R.string.error_pulse_market_mood)
                    ),
                    onCoinClick = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market Mood").assertIsDisplayed()
            onNodeWithText("Market mood unavailable").assertIsDisplayed()
        }
    }

    @Test
    fun when_globalMarketErrorWithExistingContent_should_showSnackbar() {
        composeTestRule.setContent {
            AppTheme {
                PulseScreen(
                    uiState = PulseUiState(
                        fearGreed = fearGreed(),
                        errorMessageIds = setOf(R.string.error_pulse_global_market)
                    ),
                    onCoinClick = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market Mood").assertIsDisplayed()
            onNodeWithText("Global market unavailable").assertIsDisplayed()
        }
    }

    @Test
    fun when_uiStateHasTrendingCoins_should_showTrendingContent() {
        composeTestRule.setContent {
            AppTheme {
                PulseScreen(
                    uiState = PulseUiState(
                        fearGreed = fearGreed(),
                        trendingCoins = trendingCoins()
                    ),
                    onCoinClick = {},
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Trending Now").performScrollTo().assertIsDisplayed()
            onNodeWithText("Bitcoin").assertIsDisplayed()
            onNodeWithText("#1 Trending").performScrollTo().assertIsDisplayed()
            onNodeWithText("ETH").assertExists()
        }
    }

    @Test
    fun when_trendingSpotlightClicked_should_callOnCoinClickWithId() {
        var clickedCoinId: String? = null

        composeTestRule.setContent {
            AppTheme {
                PulseScreen(
                    uiState = PulseUiState(
                        fearGreed = fearGreed(),
                        trendingCoins = trendingCoins()
                    ),
                    onCoinClick = { clickedCoinId = it },
                    onRefresh = {},
                    onDismissError = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Bitcoin").performScrollTo().performClick()

        assertThat(clickedCoinId).isEqualTo("Qwsogvtv82FCd")
    }

    private fun trendingCoins(): ImmutableList<TrendingCoin> =
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
            ),
            TrendingCoin(
                id = "razxDUgYGNAdQ",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1875.473083380222", Currency.USD),
                priceChangePercentage24h = Percentage("-1.84"),
                sparkline = persistentListOf()
            )
        ).toPersistentList()

    private fun fearGreed(value: Int = 42): FearGreed =
        FearGreed(
            value = value,
            moodBand = FearGreedMoodBand.Fear,
            history = persistentListOf(
                BigDecimal("35"),
                BigDecimal("40"),
                BigDecimal(value.toString())
            )
        )

    private fun globalMarket(): GlobalMarket =
        GlobalMarket(
            totalMarketCap = Price("2410000000000", Currency.USD),
            volume24h = Price("98200000000", Currency.USD),
            btcDominancePercentage = BigDecimal("54.2"),
            coinsUp24h = 2841,
            coinsDown24h = 1893
        )
}

package dev.shorthouse.coinwatch.ui.screen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import dev.shorthouse.coinwatch.ui.screen.details.CoinDetailsScreen
import dev.shorthouse.coinwatch.ui.screen.details.DetailsUiState
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class CoinDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_uiStateLoading_should_showSkeletonLoader() {
        val uiStateLoading = DetailsUiState.Loading

        composeTestRule.setContent {
            AppTheme {
                CoinDetailsScreen(
                    uiState = uiStateLoading,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").assertIsDisplayed()
            onNodeWithContentDescription("Favourite").assertIsDisplayed()
            onNodeWithText("Chart Range").assertIsDisplayed()
            onNodeWithText("Market Stats").assertExists()
        }
    }

    @Test
    fun when_uiStateError_should_showErrorState() {
        val uiStateError = DetailsUiState.Error("Error message")

        composeTestRule.setContent {
            AppTheme {
                CoinDetailsScreen(
                    uiState = uiStateError,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("An error has occurred").assertIsDisplayed()
            onNodeWithText("Error message").assertIsDisplayed()
            onNodeWithText("Retry").assertIsDisplayed()
            onNodeWithText("Retry").assertHasClickAction()
        }
    }

    @Test
    fun when_uiStateErrorRetryClicked_should_callOnRefresh() {
        var onRefreshCalled = false
        val uiStateError = DetailsUiState.Error("Error message")

        composeTestRule.setContent {
            AppTheme {
                CoinDetailsScreen(
                    uiState = uiStateError,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {},
                    onRefresh = { onRefreshCalled = true }
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Retry").performClick()
        }

        assertThat(onRefreshCalled).isTrue()
    }

    @Test
    fun when_uiStateErrorBackClicked_should_callOnNavigateUp() {
        var onNavigateUpCalled = false
        val uiStateError = DetailsUiState.Error("Error message")

        composeTestRule.setContent {
            AppTheme {
                CoinDetailsScreen(
                    uiState = uiStateError,
                    onNavigateUp = { onNavigateUpCalled = true },
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").performClick()
        }

        assertThat(onNavigateUpCalled).isTrue()
    }

    @Test
    fun when_uiStateSuccess_should_showExpectedContent() {
        val uiStateSuccess = DetailsUiState.Success(
            CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                marketCapRank = "2",
                volume24h = "6,627,669,115",
                circulatingSupply = "120,186,525",
                allTimeHigh = Price("4878.26"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                prices = persistentListOf(
                    BigDecimal("1755.19"),
                    BigDecimal("1749.71"),
                    BigDecimal("1750.94"),
                    BigDecimal("1748.44"),
                    BigDecimal("1743.98"),
                    BigDecimal("1740.25")
                ),
                minPrice = Price("1632.46"),
                maxPrice = Price("1922.83"),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                CoinDetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").assertIsDisplayed()
            onNodeWithContentDescription("Favourite").assertIsDisplayed()

            onNodeWithText("Ethereum").assertIsDisplayed()
            onNodeWithText("ETH").assertIsDisplayed()
            onNodeWithText("$1,879.14").assertIsDisplayed()
            onNodeWithText("+7.06%").assertIsDisplayed()
            onNodeWithText("Past day").assertIsDisplayed()

            onNodeWithText("1H").assertIsDisplayed()
            onNodeWithText("1D").assertIsDisplayed()
            onNodeWithText("1W").assertIsDisplayed()
            onNodeWithText("1M").assertIsDisplayed()
            onNodeWithText("3M").assertIsDisplayed()
            onNodeWithText("1Y").assertIsDisplayed()
            onNodeWithText("5Y").assertIsDisplayed()

            onNodeWithText("Chart Range").assertIsDisplayed()
            onNodeWithText("Low").assertIsDisplayed()
            onNodeWithText("$1,632.46").assertIsDisplayed()
            onNodeWithText("High").assertIsDisplayed()
            onNodeWithText("$1,922.83").assertIsDisplayed()

            onNodeWithTag("coin_details_content")
                .performTouchInput { swipeUp(durationMillis = 500) }

            onNodeWithText("Market Stats").assertIsDisplayed()
            onNodeWithText("Market Cap Rank").assertIsDisplayed()
            onNodeWithText("2").assertIsDisplayed()
            onNodeWithText("Market Cap").assertIsDisplayed()
            onNodeWithText("$225,722,901,094.00").assertIsDisplayed()
            onNodeWithText("Volume 24h").assertIsDisplayed()
            onNodeWithText("6,627,669,115").assertIsDisplayed()
            onNodeWithText("Circulating Supply").assertIsDisplayed()
            onNodeWithText("120,186,525").assertIsDisplayed()
            onNodeWithText("All Time High").assertIsDisplayed()
            onNodeWithText("$4,878.26").assertIsDisplayed()
            onNodeWithText("All Time High Date").assertIsDisplayed()
            onNodeWithText("10 Nov 2021").assertIsDisplayed()
            onNodeWithText("Listed Date").assertIsDisplayed()
            onNodeWithText("7 Aug 2015").assertIsDisplayed()
        }
    }

    @Test
    fun when_backClicked_should_callOnNavigateUp() {
        var onNavigateUpCalled = false

        val uiStateSuccess = DetailsUiState.Success(
            CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                marketCapRank = "2",
                volume24h = "6,627,669,115",
                circulatingSupply = "120,186,525",
                allTimeHigh = Price("4878.26"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                prices = persistentListOf(
                    BigDecimal("1755.19"),
                    BigDecimal("1749.71"),
                    BigDecimal("1750.94"),
                    BigDecimal("1748.44"),
                    BigDecimal("1743.98"),
                    BigDecimal("1740.25")
                ),
                minPrice = Price("1632.46"),
                maxPrice = Price("1922.83"),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                CoinDetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = { onNavigateUpCalled = true },
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").performClick()
        }

        assertThat(onNavigateUpCalled).isTrue()
    }

    @Test
    fun when_favouriteCoinClicked_should_callOnClickFavouriteCoin() {
        var onClickFavouriteCoinCalled = false

        val uiStateSuccess = DetailsUiState.Success(
            CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                marketCapRank = "2",
                volume24h = "6,627,669,115",
                circulatingSupply = "120,186,525",
                allTimeHigh = Price("4878.26"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                prices = persistentListOf(
                    BigDecimal("1755.19"),
                    BigDecimal("1749.71"),
                    BigDecimal("1750.94"),
                    BigDecimal("1748.44"),
                    BigDecimal("1743.98"),
                    BigDecimal("1740.25")
                ),
                minPrice = Price("1632.46"),
                maxPrice = Price("1922.83"),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                CoinDetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = { onClickFavouriteCoinCalled = true },
                    onClickChartPeriod = {},
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Favourite").performClick()
        }

        assertThat(onClickFavouriteCoinCalled).isTrue()
    }

    @Test
    fun when_chartPeriodsClicked_should_callOnClickChartPeriod() {
        val onClickChartPeriodMap = ChartPeriod.values()
            .associateWith { false }
            .toMutableMap()

        val uiStateSuccess = DetailsUiState.Success(
            CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                marketCapRank = "2",
                volume24h = "6,627,669,115",
                circulatingSupply = "120,186,525",
                allTimeHigh = Price("4878.26"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                prices = persistentListOf(
                    BigDecimal("1755.19"),
                    BigDecimal("1749.71"),
                    BigDecimal("1750.94"),
                    BigDecimal("1748.44"),
                    BigDecimal("1743.98"),
                    BigDecimal("1740.25")
                ),
                minPrice = Price("1632.46"),
                maxPrice = Price("1922.83"),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                CoinDetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = { onClickChartPeriodMap[it] = true },
                    onRefresh = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("1H").performClick()
            onNodeWithText("1D").performClick()
            onNodeWithText("1W").performClick()
            onNodeWithText("1M").performClick()
            onNodeWithText("3M").performClick()
            onNodeWithText("1Y").performClick()
            onNodeWithText("5Y").performClick()
        }

        onClickChartPeriodMap.values.forEach { isChartPeriodClicked ->
            assertThat(isChartPeriodClicked).isTrue()
        }
    }
}

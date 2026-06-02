package dev.shorthouse.coinwatch.ui.screen

import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.PriceEntry
import dev.shorthouse.coinwatch.rule.LocaleRule
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import dev.shorthouse.coinwatch.ui.screen.details.DetailsContent
import dev.shorthouse.coinwatch.ui.screen.details.DetailsScreen
import dev.shorthouse.coinwatch.ui.screen.details.DetailsUiState
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class DetailsScreenTest {

    @get:Rule(order = 0)
    val localeRule = LocaleRule()

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun when_uiStateLoading_should_showLoadingIndicator() {
        val uiStateLoading = DetailsUiState.Loading

        composeTestRule.setContent {
            AppTheme {
                DetailsScreen(
                    uiState = uiStateLoading,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").assertExists()
            onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo))
                .assertExists()
        }
    }

    @Test
    fun when_uiStateError_should_showErrorState() {
        val uiStateError = DetailsUiState.Error("Error message")

        composeTestRule.setContent {
            AppTheme {
                DetailsScreen(
                    uiState = uiStateError,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("An error has occurred").assertExists()
            onNodeWithText("Error message").assertExists()
        }
    }

    @Test
    fun when_uiStateErrorBackClicked_should_callOnNavigateUp() {
        var onNavigateUpCalled = false
        val uiStateError = DetailsUiState.Error("Error message")

        composeTestRule.setContent {
            AppTheme {
                DetailsScreen(
                    uiState = uiStateError,
                    onNavigateUp = { onNavigateUpCalled = true },
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {}
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
                description = "Ethereum is a decentralized blockchain with smart contract functionality.",
                tags = persistentListOf("smart-contracts", "staking", "layer-1"),
                links = persistentListOf(
                    CoinLink(
                        type = CoinLinkType.Website,
                        url = "https://ethereum.org",
                    ),
                    CoinLink(
                        type = CoinLinkType.GitHub,
                        url = "https://github.com/ethereum",
                    ),
                    CoinLink(
                        type = CoinLinkType.Reddit,
                        url = "https://reddit.com/r/ethereum",
                    )
                ),
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                fullyDilutedMarketCap = Price("250000000000"),
                marketCapRank = "2",
                volume24h = Price("6627669115"),
                numberOfExchanges = "248",
                numberOfMarkets = "1,098",
                circulatingSupply = "120,186,525",
                totalSupply = "120,500,000",
                maxSupply = "210,000,000",
                allTimeHigh = Price("4878.26"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                currency = Currency.USD,
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
                ),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                DetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").assertExists()
            onNodeWithContentDescription("Favourite").assertExists()

            onNodeWithText("Ethereum").assertExists()
            onNodeWithText("ETH").assertExists()
            onNodeWithText("$1,879.14").assertExists()
            onNodeWithText("+7.06%").assertExists()
            onNodeWithText("Past day").assertExists()

            onNodeWithText("1H").assertExists()
            onNodeWithText("1D").assertExists()
            onNodeWithText("1W").assertExists()
            onNodeWithText("1M").assertExists()
            onNodeWithText("3M").assertExists()
            onNodeWithText("1Y").assertExists()
            onNodeWithText("5Y").assertExists()

            onNodeWithText("Market Stats").assertExists()
            onNodeWithText("Market Cap Rank").assertExists()
            onNodeWithText("2").assertExists()
            onNodeWithText("Market Cap").assertExists()
            onNodeWithText("$225.72B").assertExists()
            onNodeWithText("Fully Diluted Market Cap").assertExists()
            onNodeWithText("$250.00B").assertExists()
            onNodeWithText("All Time High").assertExists()
            onNodeWithText("$4,878.26").assertExists()
            onNodeWithText("All Time High Date").assertExists()
            onNodeWithText("10 Nov 2021").assertExists()
            onNodeWithText("Volume (24h)").assertExists()
            onNodeWithText("$6.63B").assertExists()
            onNodeWithText("Exchange Listings").assertExists()
            onNodeWithText("248").assertExists()
            onNodeWithText("Market Listings").assertExists()
            onNodeWithText("1,098").assertExists()
            onNodeWithText("Supply").assertExists()
            onNodeWithText("Circulating Supply").assertExists()
            onNodeWithText("120,186,525").assertExists()
            onNodeWithText("Total Supply").assertExists()
            onNodeWithText("120,500,000").assertExists()
            onNodeWithText("Max Supply").assertExists()
            onNodeWithText("210,000,000").assertExists()
            onNodeWithText("About").assertExists()
            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .assertExists()
            onNodeWithText("Tags").assertExists()
            onNodeWithText("smart-contracts").assertExists()
            onNodeWithText("staking").assertExists()
            onNodeWithText("layer-1").assertExists()
            onNodeWithText("Listed Date").assertExists()
            onNodeWithText("7 Aug 2015").assertExists()
            onNodeWithText("Links").assertExists()
            onNodeWithText("Website").assertExists()
            onNodeWithText("GitHub").assertExists()
            onNodeWithText("Reddit").assertExists()
        }
    }

    @Test
    fun when_linksAreEmpty_should_notShowLinksSection() {
        composeTestRule.setContent {
            AppTheme {
                DetailsContent(
                    coinDetails = CoinDetails(
                        id = "ethereum",
                        name = "Ethereum",
                        symbol = "ETH",
                        description = "Ethereum is a decentralized blockchain with smart contract functionality.",
                        tags = persistentListOf("smart-contracts"),
                        links = persistentListOf(),
                        imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                        currentPrice = Price("1879.14"),
                        marketCap = Price("225722901094"),
                        fullyDilutedMarketCap = Price("250000000000"),
                        marketCapRank = "2",
                        volume24h = Price("6627669115"),
                        numberOfExchanges = "248",
                        numberOfMarkets = "1,098",
                        circulatingSupply = "120,186,525",
                        totalSupply = "120,500,000",
                        maxSupply = "210,000,000",
                        allTimeHigh = Price("4878.26"),
                        allTimeHighDate = "10 Nov 2021",
                        listedDate = "7 Aug 2015"
                    ),
                    coinChart = CoinChart(
                        currency = Currency.USD,
                        priceHistory = persistentListOf(),
                        periodPriceChangePercentage = Percentage("7.06")
                    ),
                    chartPeriod = ChartPeriod.Day,
                    onClickChartPeriod = {},
                    scrollState = rememberScrollState(),
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("About").assertExists()
            onAllNodesWithText("Links").assertCountEquals(0)
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
                description = "Ethereum is a decentralized blockchain with smart contract functionality.",
                tags = persistentListOf("smart-contracts", "staking", "layer-1"),
                links = persistentListOf(),
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                fullyDilutedMarketCap = Price("250000000000"),
                marketCapRank = "2",
                volume24h = Price("6627669115"),
                numberOfExchanges = "248",
                numberOfMarkets = "1,098",
                circulatingSupply = "120,186,525",
                totalSupply = "120,500,000",
                maxSupply = "210,000,000",
                allTimeHigh = Price("4878.26"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                currency = Currency.USD,
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
                ),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                DetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = { onNavigateUpCalled = true },
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {}
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
                description = "Ethereum is a decentralized blockchain with smart contract functionality.",
                tags = persistentListOf("smart-contracts", "staking", "layer-1"),
                links = persistentListOf(),
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                fullyDilutedMarketCap = Price("250000000000"),
                marketCapRank = "2",
                volume24h = Price("6627669115"),
                numberOfExchanges = "248",
                numberOfMarkets = "1,098",
                circulatingSupply = "120,186,525",
                totalSupply = "120,500,000",
                maxSupply = "210,000,000",
                allTimeHigh = Price("4878.26"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                currency = Currency.USD,
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
                ),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                DetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = { onClickFavouriteCoinCalled = true },
                    onClickChartPeriod = {}
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
        val onClickChartPeriodMap = ChartPeriod.entries
            .associateWith { false }
            .toMutableMap()

        val uiStateSuccess = DetailsUiState.Success(
            CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                description = "Ethereum is a decentralized blockchain with smart contract functionality.",
                tags = persistentListOf("smart-contracts", "staking", "layer-1"),
                links = persistentListOf(),
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                fullyDilutedMarketCap = Price("250000000000"),
                marketCapRank = "2",
                volume24h = Price("6627669115"),
                numberOfExchanges = "248",
                numberOfMarkets = "1,098",
                circulatingSupply = "120,186,525",
                totalSupply = "120,500,000",
                maxSupply = "210,000,000",
                allTimeHigh = Price("4878.26"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                currency = Currency.USD,
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
                ),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                DetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = { onClickChartPeriodMap[it] = true }
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("1H").performClick()
            waitForIdle()
            mainClock.advanceTimeBy(1000)

            onNodeWithText("1D").performClick()
            waitForIdle()
            mainClock.advanceTimeBy(1000)

            onNodeWithText("1W").performClick()
            waitForIdle()
            mainClock.advanceTimeBy(1000)

            onNodeWithText("1M").performClick()
            waitForIdle()
            mainClock.advanceTimeBy(1000)

            onNodeWithText("3M").performClick()
            waitForIdle()
            mainClock.advanceTimeBy(1000)

            onNodeWithText("1Y").performClick()
            waitForIdle()
            mainClock.advanceTimeBy(1000)

            onNodeWithText("5Y").performClick()
            waitForIdle()
            mainClock.advanceTimeBy(1000)
        }

        onClickChartPeriodMap.values.forEach { isChartPeriodClicked ->
            assertThat(isChartPeriodClicked).isTrue()
        }
    }

    @Test
    fun when_coinPricesChartEmpty_should_showEmptyState() {
        val uiStateSuccess = DetailsUiState.Success(
            CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                description = "Ethereum is a decentralized blockchain with smart contract functionality.",
                tags = persistentListOf("smart-contracts", "staking", "layer-1"),
                links = persistentListOf(),
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                fullyDilutedMarketCap = Price("250000000000"),
                marketCapRank = "2",
                volume24h = Price("6627669115"),
                numberOfExchanges = "248",
                numberOfMarkets = "1,098",
                circulatingSupply = "120,186,525",
                totalSupply = "120,500,000",
                maxSupply = "210,000,000",
                allTimeHigh = Price("4878.26"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                currency = Currency.USD,
                priceHistory = persistentListOf(),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                DetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("No chart data available").assertExists()
            onNodeWithText("1H").assertExists()
            onNodeWithText("1D").assertExists()
            onNodeWithText("1W").assertExists()
            onNodeWithText("1M").assertExists()
            onNodeWithText("3M").assertExists()
            onNodeWithText("1Y").assertExists()
            onNodeWithText("5Y").assertExists()
        }
    }

    @Test
    fun when_currencyNotDollars_should_showExpectedCurrencyForATH() {
        val currency = Currency.GBP

        val uiStateSuccess = DetailsUiState.Success(
            CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                description = "Ethereum is a decentralized blockchain with smart contract functionality.",
                tags = persistentListOf("smart-contracts", "staking", "layer-1"),
                links = persistentListOf(),
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14", currency = currency),
                marketCap = Price("225722901094", currency = currency),
                fullyDilutedMarketCap = Price("250000000000", currency = currency),
                marketCapRank = "2",
                volume24h = Price("6627669115"),
                numberOfExchanges = "248",
                numberOfMarkets = "1,098",
                circulatingSupply = "120,186,525",
                totalSupply = "120,500,000",
                maxSupply = "210,000,000",
                allTimeHigh = Price("4878.26", currency = currency),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                currency = currency,
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
                ),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                DetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").assertExists()
            onNodeWithContentDescription("Favourite").assertExists()

            onNodeWithText("Ethereum").assertExists()
            onNodeWithText("ETH").assertExists()
            onNodeWithText("£1,879.14").assertExists()
            onNodeWithText("+7.06%").assertExists()
            onNodeWithText("Past day").assertExists()

            onNodeWithText("1H").assertExists()
            onNodeWithText("1D").assertExists()
            onNodeWithText("1W").assertExists()
            onNodeWithText("1M").assertExists()
            onNodeWithText("3M").assertExists()
            onNodeWithText("1Y").assertExists()
            onNodeWithText("5Y").assertExists()

            onNodeWithText("Market Stats").assertExists()
            onNodeWithText("Market Cap Rank").assertExists()
            onNodeWithText("2").assertExists()
            onNodeWithText("Market Cap").assertExists()
            onNodeWithText("£225.72B").assertExists()
            onNodeWithText("Fully Diluted Market Cap").assertExists()
            onNodeWithText("£250.00B").assertExists()
            onNodeWithText("All Time High").assertExists()
            onNodeWithText("£4,878.26").assertExists()
            onNodeWithText("All Time High Date").assertExists()
            onNodeWithText("10 Nov 2021").assertExists()
            onNodeWithText("Volume (24h)").assertExists()
            onNodeWithText("$6.63B").assertExists()
            onNodeWithText("Exchange Listings").assertExists()
            onNodeWithText("248").assertExists()
            onNodeWithText("Market Listings").assertExists()
            onNodeWithText("1,098").assertExists()
            onNodeWithText("Supply").assertExists()
            onNodeWithText("Circulating Supply").assertExists()
            onNodeWithText("120,186,525").assertExists()
            onNodeWithText("Total Supply").assertExists()
            onNodeWithText("120,500,000").assertExists()
            onNodeWithText("Max Supply").assertExists()
            onNodeWithText("210,000,000").assertExists()
            onNodeWithText("About").assertExists()
            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .assertExists()
            onNodeWithText("Tags").assertExists()
            onNodeWithText("smart-contracts").assertExists()
            onNodeWithText("staking").assertExists()
            onNodeWithText("layer-1").assertExists()
            onNodeWithText("Listed Date").assertExists()
            onNodeWithText("7 Aug 2015").assertExists()
        }
    }

    @Test
    fun when_pricesAreLarge_should_showShortenedPrices() {
        val uiStateSuccess = DetailsUiState.Success(
            CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                description = "Ethereum is a decentralized blockchain with smart contract functionality.",
                tags = persistentListOf("smart-contracts", "staking", "layer-1"),
                links = persistentListOf(),
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("49491394.23440234"),
                fullyDilutedMarketCap = Price("3094938574102"),
                marketCapRank = "2",
                volume24h = Price("1009900243"),
                numberOfExchanges = "18,294",
                numberOfMarkets = "1,234,567",
                circulatingSupply = "120,186,525",
                totalSupply = "120,500,000",
                maxSupply = "210,000,000",
                allTimeHigh = Price("3084938574102"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            ),
            CoinChart(
                currency = Currency.USD,
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
                ),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = true
        )

        composeTestRule.setContent {
            AppTheme {
                DetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Back").assertExists()
            onNodeWithContentDescription("Favourite").assertExists()

            onNodeWithText("Ethereum").assertExists()
            onNodeWithText("ETH").assertExists()
            onNodeWithText("$1,879.14").assertExists()
            onNodeWithText("+7.06%").assertExists()
            onNodeWithText("Past day").assertExists()

            onNodeWithText("1H").assertExists()
            onNodeWithText("1D").assertExists()
            onNodeWithText("1W").assertExists()
            onNodeWithText("1M").assertExists()
            onNodeWithText("3M").assertExists()
            onNodeWithText("1Y").assertExists()
            onNodeWithText("5Y").assertExists()

            onNodeWithText("Market Stats").assertExists()
            onNodeWithText("Market Cap Rank").assertExists()
            onNodeWithText("2").assertExists()
            onNodeWithText("Market Cap").assertExists()
            onNodeWithText("$49.49M").assertExists()
            onNodeWithText("Fully Diluted Market Cap").assertExists()
            onNodeWithText("$3.09T").assertExists()
            onNodeWithText("All Time High").assertExists()
            onNodeWithText("$3.08T").assertExists()
            onNodeWithText("All Time High Date").assertExists()
            onNodeWithText("10 Nov 2021").assertExists()
            onNodeWithText("Volume (24h)").assertExists()
            onNodeWithText("$1.01B").assertExists()
            onNodeWithText("Exchange Listings").assertExists()
            onNodeWithText("18,294").assertExists()
            onNodeWithText("Market Listings").assertExists()
            onNodeWithText("1,234,567").assertExists()
            onNodeWithText("Supply").assertExists()
            onNodeWithText("Circulating Supply").assertExists()
            onNodeWithText("120,186,525").assertExists()
            onNodeWithText("Total Supply").assertExists()
            onNodeWithText("120,500,000").assertExists()
            onNodeWithText("Max Supply").assertExists()
            onNodeWithText("210,000,000").assertExists()
            onNodeWithText("About").assertExists()
            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .assertExists()
            onNodeWithText("Tags").assertExists()
            onNodeWithText("smart-contracts").assertExists()
            onNodeWithText("staking").assertExists()
            onNodeWithText("layer-1").assertExists()
            onNodeWithText("Listed Date").assertExists()
            onNodeWithText("7 Aug 2015").assertExists()
        }
    }
}

package dev.shorthouse.coinwatch.ui.screen

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.PriceEntry
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

    @get:Rule
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
            onNodeWithContentDescription("Back").assertIsDisplayed()
            onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo))
                .assertIsDisplayed()
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
            onNodeWithText("An error has occurred").assertIsDisplayed()
            onNodeWithText("Error message").assertIsDisplayed()
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
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
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
                DetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {}
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

            onNodeWithText("Chart Range").performScrollTo()
            onNodeWithText("Chart Range").assertIsDisplayed()
            onNodeWithText("Low").performScrollTo()
            onNodeWithText("Low").assertIsDisplayed()
            onNodeWithText("$1,632.46").performScrollTo()
            onNodeWithText("$1,632.46").assertIsDisplayed()
            onNodeWithText("High").performScrollTo()
            onNodeWithText("High").assertIsDisplayed()
            onNodeWithText("$1,922.83").performScrollTo()
            onNodeWithText("$1,922.83").assertIsDisplayed()

            onNodeWithText("Market Stats").performScrollTo()

            onNodeWithText("Market Stats").assertIsDisplayed()
            onNodeWithText("Market Cap Rank").assertIsDisplayed()
            onNodeWithText("2").assertIsDisplayed()
            onNodeWithText("Market Cap").assertIsDisplayed()
            onNodeWithText("$225.72B").assertIsDisplayed()
            onNodeWithText("Fully Diluted Market Cap").assertIsDisplayed()
            onNodeWithText("$250.00B").assertIsDisplayed()
            onNodeWithText("All Time High").performScrollTo()
            onNodeWithText("All Time High").assertIsDisplayed()
            onNodeWithText("$4,878.26").assertIsDisplayed()
            onNodeWithText("All Time High Date").performScrollTo()
            onNodeWithText("All Time High Date").assertIsDisplayed()
            onNodeWithText("10 Nov 2021").assertIsDisplayed()
            onNodeWithText("Volume (24h)").performScrollTo()
            onNodeWithText("Volume (24h)").assertIsDisplayed()
            onNodeWithText("$6.63B").assertIsDisplayed()
            onNodeWithText("Exchange Listings").performScrollTo()
            onNodeWithText("Exchange Listings").assertIsDisplayed()
            onNodeWithText("248").assertIsDisplayed()
            onNodeWithText("Market Listings").performScrollTo()
            onNodeWithText("Market Listings").assertIsDisplayed()
            onNodeWithText("1,098").assertIsDisplayed()
            onNodeWithText("Supply").performScrollTo()
            onNodeWithText("Supply").assertIsDisplayed()

            onNodeWithText("Circulating Supply").performScrollTo()
            onNodeWithText("Circulating Supply").assertIsDisplayed()
            onNodeWithText("120,186,525").assertIsDisplayed()

            onNodeWithText("Total Supply").performScrollTo()
            onNodeWithText("Total Supply").assertIsDisplayed()
            onNodeWithText("120,500,000").assertIsDisplayed()

            onNodeWithText("Max Supply").performScrollTo()
            onNodeWithText("Max Supply").assertIsDisplayed()
            onNodeWithText("210,000,000").assertIsDisplayed()

            onNodeWithText("About").performScrollTo()
            onNodeWithText("About").assertIsDisplayed()

            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .performScrollTo()
            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .assertIsDisplayed()
            onNodeWithText("Tags").performScrollTo()
            onNodeWithText("Tags").assertIsDisplayed()
            onNodeWithText("smart-contracts").performScrollTo()
            onNodeWithText("smart-contracts").assertIsDisplayed()
            onNodeWithText("staking").performScrollTo()
            onNodeWithText("staking").assertIsDisplayed()
            onNodeWithText("layer-1").performScrollTo()
            onNodeWithText("layer-1").assertIsDisplayed()
            onNodeWithText("Listed Date").performScrollTo()
            onNodeWithText("Listed Date").assertIsDisplayed()
            onNodeWithText("7 Aug 2015").assertIsDisplayed()

            onNodeWithText("Links").performScrollTo()
            onNodeWithText("Links").assertIsDisplayed()
            onNodeWithText("Website").performScrollTo()
            onNodeWithText("Website").assertIsDisplayed()
            onNodeWithText("GitHub").performScrollTo()
            onNodeWithText("GitHub").assertIsDisplayed()
            onNodeWithText("Reddit").performScrollTo()
            onNodeWithText("Reddit").assertIsDisplayed()
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
                        priceHistory = persistentListOf(),
                        minPrice = Price("1632.46"),
                        maxPrice = Price("1922.83"),
                        periodPriceChangePercentage = Percentage("7.06")
                    ),
                    chartPeriod = ChartPeriod.Day,
                    onClickChartPeriod = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("About").performScrollTo()
            onNodeWithText("About").assertIsDisplayed()
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
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
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
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
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
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
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
            onNodeWithText("1D").performClick()
            waitForIdle()
            onNodeWithText("1W").performClick()
            waitForIdle()
            onNodeWithText("1M").performClick()
            waitForIdle()
            onNodeWithText("3M").performClick()
            waitForIdle()
            onNodeWithText("1Y").performClick()
            waitForIdle()
            onNodeWithText("5Y").performClick()
            waitForIdle()
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
                priceHistory = persistentListOf(),
                minPrice = Price("1632.46"),
                maxPrice = Price("1922.83"),
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
            onNodeWithText("No chart data available").assertIsDisplayed()
            onNodeWithText("1H").assertIsDisplayed()
            onNodeWithText("1D").assertIsDisplayed()
            onNodeWithText("1W").assertIsDisplayed()
            onNodeWithText("1M").assertIsDisplayed()
            onNodeWithText("3M").assertIsDisplayed()
            onNodeWithText("1Y").assertIsDisplayed()
            onNodeWithText("5Y").assertIsDisplayed()
        }
    }

    @Test
    fun when_coinChartRangeEmpty_should_showEmptyState() {
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
                priceHistory = persistentListOf(),
                minPrice = Price(null),
                maxPrice = Price(null),
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
            onNodeWithText("No chart range data available").performScrollTo()
            onNodeWithText("No chart range data available").assertIsDisplayed()
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
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
                ),
                minPrice = Price("1632.46", currency = currency),
                maxPrice = Price("1922.83", currency = currency),
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
            onNodeWithContentDescription("Back").assertIsDisplayed()
            onNodeWithContentDescription("Favourite").assertIsDisplayed()

            onNodeWithText("Ethereum").assertIsDisplayed()
            onNodeWithText("ETH").assertIsDisplayed()
            onNodeWithText("£1,879.14").assertIsDisplayed()
            onNodeWithText("+7.06%").assertIsDisplayed()
            onNodeWithText("Past day").assertIsDisplayed()

            onNodeWithText("1H").assertIsDisplayed()
            onNodeWithText("1D").assertIsDisplayed()
            onNodeWithText("1W").assertIsDisplayed()
            onNodeWithText("1M").assertIsDisplayed()
            onNodeWithText("3M").assertIsDisplayed()
            onNodeWithText("1Y").assertIsDisplayed()
            onNodeWithText("5Y").assertIsDisplayed()

            onNodeWithText("Chart Range").performScrollTo()
            onNodeWithText("Chart Range").assertIsDisplayed()
            onNodeWithText("Low").performScrollTo()
            onNodeWithText("Low").assertIsDisplayed()
            onNodeWithText("£1,632.46").performScrollTo()
            onNodeWithText("£1,632.46").assertIsDisplayed()
            onNodeWithText("High").performScrollTo()
            onNodeWithText("High").assertIsDisplayed()
            onNodeWithText("£1,922.83").performScrollTo()
            onNodeWithText("£1,922.83").assertIsDisplayed()

            onNodeWithText("Market Stats").performScrollTo()

            onNodeWithText("Market Stats").assertIsDisplayed()
            onNodeWithText("Market Cap Rank").assertIsDisplayed()
            onNodeWithText("2").assertIsDisplayed()
            onNodeWithText("Market Cap").assertIsDisplayed()
            onNodeWithText("£225.72B").assertIsDisplayed()
            onNodeWithText("Fully Diluted Market Cap").assertIsDisplayed()
            onNodeWithText("£250.00B").assertIsDisplayed()
            onNodeWithText("All Time High").performScrollTo()
            onNodeWithText("All Time High").assertIsDisplayed()
            onNodeWithText("£4,878.26").assertIsDisplayed()
            onNodeWithText("All Time High Date").performScrollTo()
            onNodeWithText("All Time High Date").assertIsDisplayed()
            onNodeWithText("10 Nov 2021").assertIsDisplayed()
            onNodeWithText("Volume (24h)").performScrollTo()
            onNodeWithText("Volume (24h)").assertIsDisplayed()
            onNodeWithText("$6.63B").assertIsDisplayed()
            onNodeWithText("Exchange Listings").performScrollTo()
            onNodeWithText("Exchange Listings").assertIsDisplayed()
            onNodeWithText("248").assertIsDisplayed()
            onNodeWithText("Market Listings").performScrollTo()
            onNodeWithText("Market Listings").assertIsDisplayed()
            onNodeWithText("1,098").assertIsDisplayed()
            onNodeWithText("Supply").performScrollTo()
            onNodeWithText("Supply").assertIsDisplayed()

            onNodeWithText("Circulating Supply").performScrollTo()
            onNodeWithText("Circulating Supply").assertIsDisplayed()
            onNodeWithText("120,186,525").assertIsDisplayed()

            onNodeWithText("Total Supply").performScrollTo()
            onNodeWithText("Total Supply").assertIsDisplayed()
            onNodeWithText("120,500,000").assertIsDisplayed()

            onNodeWithText("Max Supply").performScrollTo()
            onNodeWithText("Max Supply").assertIsDisplayed()
            onNodeWithText("210,000,000").assertIsDisplayed()

            onNodeWithText("About").performScrollTo()
            onNodeWithText("About").assertIsDisplayed()

            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .performScrollTo()
            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .assertIsDisplayed()
            onNodeWithText("Tags").performScrollTo()
            onNodeWithText("Tags").assertIsDisplayed()
            onNodeWithText("smart-contracts").performScrollTo()
            onNodeWithText("smart-contracts").assertIsDisplayed()
            onNodeWithText("staking").performScrollTo()
            onNodeWithText("staking").assertIsDisplayed()
            onNodeWithText("layer-1").performScrollTo()
            onNodeWithText("layer-1").assertIsDisplayed()
            onNodeWithText("Listed Date").performScrollTo()
            onNodeWithText("Listed Date").assertIsDisplayed()
            onNodeWithText("7 Aug 2015").assertIsDisplayed()
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
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023")
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
                DetailsScreen(
                    uiState = uiStateSuccess,
                    onNavigateUp = {},
                    onClickFavouriteCoin = {},
                    onClickChartPeriod = {}
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

            onNodeWithText("Chart Range").performScrollTo()
            onNodeWithText("Chart Range").assertIsDisplayed()
            onNodeWithText("Low").performScrollTo()
            onNodeWithText("Low").assertIsDisplayed()
            onNodeWithText("$1,632.46").performScrollTo()
            onNodeWithText("$1,632.46").assertIsDisplayed()
            onNodeWithText("High").performScrollTo()
            onNodeWithText("High").assertIsDisplayed()
            onNodeWithText("$1,922.83").performScrollTo()
            onNodeWithText("$1,922.83").assertIsDisplayed()

            onNodeWithText("Market Stats").performScrollTo()

            onNodeWithText("Market Stats").assertIsDisplayed()
            onNodeWithText("Market Cap Rank").assertIsDisplayed()
            onNodeWithText("2").assertIsDisplayed()
            onNodeWithText("Market Cap").assertIsDisplayed()
            onNodeWithText("$49.49M").assertIsDisplayed()
            onNodeWithText("Fully Diluted Market Cap").assertIsDisplayed()
            onNodeWithText("$3.09T").assertIsDisplayed()
            onNodeWithText("All Time High").performScrollTo()
            onNodeWithText("All Time High").assertIsDisplayed()
            onNodeWithText("$3.08T").assertIsDisplayed()
            onNodeWithText("All Time High Date").performScrollTo()
            onNodeWithText("All Time High Date").assertIsDisplayed()
            onNodeWithText("10 Nov 2021").assertIsDisplayed()
            onNodeWithText("Volume (24h)").performScrollTo()
            onNodeWithText("Volume (24h)").assertIsDisplayed()
            onNodeWithText("$1.01B").assertIsDisplayed()
            onNodeWithText("Exchange Listings").performScrollTo()
            onNodeWithText("Exchange Listings").assertIsDisplayed()
            onNodeWithText("18,294").assertIsDisplayed()
            onNodeWithText("Market Listings").performScrollTo()
            onNodeWithText("Market Listings").assertIsDisplayed()
            onNodeWithText("1,234,567").assertIsDisplayed()
            onNodeWithText("Supply").performScrollTo()
            onNodeWithText("Supply").assertIsDisplayed()

            onNodeWithText("Circulating Supply").performScrollTo()
            onNodeWithText("Circulating Supply").assertIsDisplayed()
            onNodeWithText("120,186,525").assertIsDisplayed()

            onNodeWithText("Total Supply").performScrollTo()
            onNodeWithText("Total Supply").assertIsDisplayed()
            onNodeWithText("120,500,000").assertIsDisplayed()

            onNodeWithText("Max Supply").performScrollTo()
            onNodeWithText("Max Supply").assertIsDisplayed()
            onNodeWithText("210,000,000").assertIsDisplayed()

            onNodeWithText("About").performScrollTo()
            onNodeWithText("About").assertIsDisplayed()

            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .performScrollTo()
            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .assertIsDisplayed()
            onNodeWithText("Tags").performScrollTo()
            onNodeWithText("Tags").assertIsDisplayed()
            onNodeWithText("smart-contracts").performScrollTo()
            onNodeWithText("smart-contracts").assertIsDisplayed()
            onNodeWithText("staking").performScrollTo()
            onNodeWithText("staking").assertIsDisplayed()
            onNodeWithText("layer-1").performScrollTo()
            onNodeWithText("layer-1").assertIsDisplayed()
            onNodeWithText("Listed Date").performScrollTo()
            onNodeWithText("Listed Date").assertIsDisplayed()
            onNodeWithText("7 Aug 2015").assertIsDisplayed()
        }
    }
}

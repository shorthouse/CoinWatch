package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Constants.MISSING_VALUE_PLACEHOLDER
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class MarketStatsCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_coinDetailsProvided_should_displayExpectedMarketStats() {
        composeTestRule.setContent {
            AppTheme {
                MarketStatsCard(coinDetails = coinDetails())
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market Cap Rank").assertIsDisplayed()
            onNodeWithText("2").assertIsDisplayed()
            onNodeWithText("Market Cap").assertIsDisplayed()
            onNodeWithText("$225.72B").assertIsDisplayed()
            onNodeWithText("Fully Diluted Market Cap").assertIsDisplayed()
            onNodeWithText("$250.00B").assertIsDisplayed()
            onNodeWithText("All Time High").assertIsDisplayed()
            onNodeWithText("$4,878.26").assertIsDisplayed()
            onNodeWithText("All Time High Date").assertIsDisplayed()
            onNodeWithText("10 Nov 2021").assertIsDisplayed()
            onNodeWithText("Volume (24h)").assertIsDisplayed()
            onNodeWithText("$6.63B").assertIsDisplayed()
            onNodeWithText("Exchange Listings").assertIsDisplayed()
            onNodeWithText("248").assertIsDisplayed()
            onNodeWithText("Market Listings").assertIsDisplayed()
            onNodeWithText("1,098").assertIsDisplayed()
        }
    }

    @Test
    fun when_currencyIsNotDollars_should_displayExpectedCurrencyValues() {
        composeTestRule.setContent {
            AppTheme {
                MarketStatsCard(
                    coinDetails = coinDetails(
                        marketCap = Price("225722901094", currency = Currency.GBP),
                        fullyDilutedMarketCap = Price("250000000000", currency = Currency.GBP),
                        volume24h = Price("6627669115", currency = Currency.GBP),
                        allTimeHigh = Price("4878.26", currency = Currency.GBP)
                    )
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("£225.72B").assertIsDisplayed()
            onNodeWithText("£250.00B").assertIsDisplayed()
            onNodeWithText("£6.63B").assertIsDisplayed()
            onNodeWithText("£4,878.26").assertIsDisplayed()
        }
    }

    @Test
    fun when_priceValuesAreMissing_should_displayPricePlaceholders() {
        composeTestRule.setContent {
            AppTheme {
                MarketStatsCard(
                    coinDetails = coinDetails(
                        marketCap = Price(null),
                        fullyDilutedMarketCap = Price(null),
                        volume24h = Price(null),
                        allTimeHigh = Price(null)
                    )
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market Cap").assertIsDisplayed()
            onNodeWithText("Fully Diluted Market Cap").assertIsDisplayed()
            onNodeWithText("Volume (24h)").assertIsDisplayed()
            onNodeWithText("All Time High").assertIsDisplayed()
            onAllNodesWithText("$—").assertCountEquals(4)
        }
    }

    @Test
    fun when_textStatsAreUnavailable_should_displayUnavailablePlaceholders() {
        composeTestRule.setContent {
            AppTheme {
                MarketStatsCard(
                    coinDetails = coinDetails(
                        marketCapRank = MISSING_VALUE_PLACEHOLDER,
                        numberOfExchanges = MISSING_VALUE_PLACEHOLDER,
                        numberOfMarkets = MISSING_VALUE_PLACEHOLDER,
                        allTimeHighDate = MISSING_VALUE_PLACEHOLDER
                    )
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market Cap Rank").assertIsDisplayed()
            onNodeWithText("Exchange Listings").assertIsDisplayed()
            onNodeWithText("Market Listings").assertIsDisplayed()
            onNodeWithText("All Time High Date").assertIsDisplayed()
            onAllNodesWithText(MISSING_VALUE_PLACEHOLDER).assertCountEquals(4)
        }
    }

    @Test
    fun when_marketStatsCardDisplayed_should_showRowsInExpectedOrder() {
        composeTestRule.setContent {
            AppTheme {
                MarketStatsCard(coinDetails = coinDetails())
            }
        }

        val labels = listOf(
            "Market Cap Rank",
            "Market Cap",
            "Fully Diluted Market Cap",
            "All Time High",
            "All Time High Date",
            "Volume (24h)",
            "Exchange Listings",
            "Market Listings"
        )
        val labelTops = labels.map { label ->
            composeTestRule.onNodeWithText(label)
                .getUnclippedBoundsInRoot()
                .top
        }

        assertThat(labelTops).isEqualTo(labelTops.sorted())
    }

    @Test
    fun when_valueTextIsLongAndWraps_should_displayFullValueAndLabels() {
        val longMarketListings = "123,456,789,012,345,678,901,234,567"

        composeTestRule.setContent {
            AppTheme {
                MarketStatsCard(
                    coinDetails = coinDetails(numberOfMarkets = longMarketListings),
                    modifier = Modifier.width(220.dp)
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Market Listings").assertIsDisplayed()
            onNodeWithText(longMarketListings).assertIsDisplayed()
            onNodeWithText("All Time High").assertIsDisplayed()
        }

        val longValueHeight = composeTestRule.onNodeWithText(longMarketListings)
            .getUnclippedBoundsInRoot()
            .let { bounds -> bounds.bottom - bounds.top }

        assertThat(longValueHeight.value).isGreaterThan(24f)
    }

    private fun coinDetails(
        marketCap: Price = Price("225722901094"),
        fullyDilutedMarketCap: Price = Price("250000000000"),
        volume24h: Price = Price("6627669115"),
        marketCapRank: String = "2",
        numberOfExchanges: String = "248",
        numberOfMarkets: String = "1,098",
        allTimeHigh: Price = Price("4878.26"),
        allTimeHighDate: String = "10 Nov 2021"
    ): CoinDetails {
        return CoinDetails(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            description = "Ethereum is a decentralized blockchain with smart contract functionality.",
            tags = persistentListOf("smart-contracts", "staking"),
            links = persistentListOf(
                CoinLink(
                    type = CoinLinkType.Website,
                    url = "https://ethereum.org",
                )
            ),
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
            currentPrice = Price("1879.14"),
            marketCap = marketCap,
            fullyDilutedMarketCap = fullyDilutedMarketCap,
            marketCapRank = marketCapRank,
            volume24h = volume24h,
            numberOfExchanges = numberOfExchanges,
            numberOfMarkets = numberOfMarkets,
            circulatingSupply = "120,186,525",
            totalSupply = "120,500,000",
            maxSupply = "210,000,000",
            allTimeHigh = allTimeHigh,
            allTimeHighDate = allTimeHighDate,
            listedDate = "7 Aug 2015"
        )
    }
}

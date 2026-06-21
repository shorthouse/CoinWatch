package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.MoverCoin
import dev.shorthouse.coinwatch.model.Movers
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class MoversSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_moversProvided_should_displayFeaturedAndMostMovement() {
        composeTestRule.setContent {
            AppTheme {
                MoversSection(movers = movers(), onCoinClick = {})
            }
        }

        composeTestRule.apply {
            onNodeWithText("TOP GAINER").assertIsDisplayed()
            onNodeWithText("TOP LOSER").assertIsDisplayed()
            onNodeWithText("MOST MOVEMENT").assertIsDisplayed()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithText("DOGE").assertIsDisplayed()
            onNodeWithText("Ethereum").assertIsDisplayed()
        }
    }

    @Test
    fun when_topGainerClicked_should_callOnCoinClickWithId() {
        var clickedCoinId: String? = null

        composeTestRule.setContent {
            AppTheme {
                MoversSection(movers = movers(), onCoinClick = { clickedCoinId = it })
            }
        }

        composeTestRule.onNodeWithText("BTC").performClick()

        assertThat(clickedCoinId).isEqualTo("gainer")
    }

    @Test
    fun when_moverRowClicked_should_callOnCoinClickWithId() {
        var clickedCoinId: String? = null

        composeTestRule.setContent {
            AppTheme {
                MoversSection(movers = movers(), onCoinClick = { clickedCoinId = it })
            }
        }

        composeTestRule.onNodeWithText("Ethereum").performClick()

        assertThat(clickedCoinId).isEqualTo("eth")
    }

    private fun movers(): Movers =
        Movers(
            topGainer = MoverCoin(
                id = "gainer",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("89086.46", Currency.USD),
                priceChangePercentage24h = Percentage("14.27"),
                sparkline = persistentListOf()
            ),
            topLoser = MoverCoin(
                id = "loser",
                name = "Dogecoin",
                symbol = "DOGE",
                imageUrl = "https://cdn.coinranking.com/H1arVnjQ_/doge.svg",
                currentPrice = Price("0.11923", Currency.USD),
                priceChangePercentage24h = Percentage("-11.62"),
                sparkline = persistentListOf()
            ),
            mostMovement = persistentListOf(
                MoverCoin(
                    id = "eth",
                    name = "Ethereum",
                    symbol = "ETH",
                    imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                    currentPrice = Price("3027.60", Currency.USD),
                    priceChangePercentage24h = Percentage("-8.43"),
                    sparkline = persistentListOf()
                )
            )
        )
}

package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.TrendingCoin
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class TrendingSpotlightCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_trendingCoinProvided_should_displayExpectedContent() {
        val trendingCoin = trendingCoin()

        composeTestRule.setContent {
            AppTheme {
                TrendingSpotlightCard(trendingCoin = trendingCoin, onCoinClick = {})
            }
        }

        composeTestRule.apply {
            onNodeWithText("Bitcoin").assertIsDisplayed()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithText("#1 Trending").assertIsDisplayed()
            onNodeWithText(trendingCoin.currentPrice.formattedAmount).assertIsDisplayed()
            onNodeWithText(trendingCoin.priceChangePercentage24h.formattedAmount).assertIsDisplayed()
        }
    }

    @Test
    fun when_cardClicked_should_callOnCoinClickWithId() {
        var clickedCoinId: String? = null
        val trendingCoin = trendingCoin()

        composeTestRule.setContent {
            AppTheme {
                TrendingSpotlightCard(
                    trendingCoin = trendingCoin,
                    onCoinClick = { clickedCoinId = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Bitcoin").performClick()

        assertThat(clickedCoinId).isEqualTo("Qwsogvtv82FCd")
    }

    private fun trendingCoin(): TrendingCoin =
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
}

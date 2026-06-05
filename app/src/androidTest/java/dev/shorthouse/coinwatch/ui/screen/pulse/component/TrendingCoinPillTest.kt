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
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class TrendingCoinPillTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_trendingCoinProvided_should_displayExpectedContent() {
        val trendingCoin = trendingCoin()

        composeTestRule.setContent {
            AppTheme {
                TrendingCoinPill(trendingCoin = trendingCoin, onCoinClick = {})
            }
        }

        composeTestRule.apply {
            onNodeWithText("ETH").assertIsDisplayed()
            onNodeWithText(trendingCoin.priceChangePercentage24h.formattedAmount).assertIsDisplayed()
        }
    }

    @Test
    fun when_pillClicked_should_callOnCoinClickWithId() {
        var clickedCoinId: String? = null
        val trendingCoin = trendingCoin()

        composeTestRule.setContent {
            AppTheme {
                TrendingCoinPill(
                    trendingCoin = trendingCoin,
                    onCoinClick = { clickedCoinId = it }
                )
            }
        }

        composeTestRule.onNodeWithText("ETH").performClick()

        assertThat(clickedCoinId).isEqualTo("razxDUgYGNAdQ")
    }

    private fun trendingCoin(): TrendingCoin =
        TrendingCoin(
            id = "razxDUgYGNAdQ",
            name = "Ethereum",
            symbol = "ETH",
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
            currentPrice = Price("1875.473083380222", Currency.USD),
            priceChangePercentage24h = Percentage("-1.84"),
            sparkline = persistentListOf()
        )
}

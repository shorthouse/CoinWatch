package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.MoverCoin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class MoverFeatureCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_moverCoinProvided_should_displayExpectedContent() {
        val moverCoin = moverCoin()

        composeTestRule.setContent {
            AppTheme {
                MoverFeatureCard(
                    moverCoin = moverCoin,
                    label = "Top gainer",
                    icon = Icons.AutoMirrored.Rounded.TrendingUp,
                    tint = PositiveGreen,
                    onCoinClick = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("TOP GAINER").assertIsDisplayed()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithText("Bitcoin").assertIsDisplayed()
            onNodeWithText(moverCoin.priceChangePercentage24h.formattedAmount).assertIsDisplayed()
            onNodeWithText(moverCoin.currentPrice.formattedAmount).assertIsDisplayed()
        }
    }

    @Test
    fun when_cardClicked_should_callOnCoinClickWithId() {
        var clickedCoinId: String? = null

        composeTestRule.setContent {
            AppTheme {
                MoverFeatureCard(
                    moverCoin = moverCoin(),
                    label = "Top gainer",
                    icon = Icons.AutoMirrored.Rounded.TrendingUp,
                    tint = PositiveGreen,
                    onCoinClick = { clickedCoinId = it }
                )
            }
        }

        composeTestRule.onNodeWithText("BTC").performClick()

        assertThat(clickedCoinId).isEqualTo("Qwsogvtv82FCd")
    }

    private fun moverCoin(): MoverCoin =
        MoverCoin(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29446.336548759988", Currency.USD),
            priceChangePercentage24h = Percentage("14.27"),
            sparkline = persistentListOf()
        )
}

package dev.shorthouse.coinwatch.ui.screen.pulse.component

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
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class MoverRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_moverCoinProvided_should_displayExpectedContent() {
        val moverCoin = moverCoin()

        composeTestRule.setContent {
            AppTheme {
                MoverRow(moverCoin = moverCoin, onCoinClick = {})
            }
        }

        composeTestRule.apply {
            onNodeWithText("Ethereum").assertIsDisplayed()
            onNodeWithText("ETH").assertIsDisplayed()
            onNodeWithText(moverCoin.currentPrice.formattedAmount).assertIsDisplayed()
            onNodeWithText(moverCoin.priceChangePercentage24h.formattedAmount).assertIsDisplayed()
        }
    }

    @Test
    fun when_rowClicked_should_callOnCoinClickWithId() {
        var clickedCoinId: String? = null

        composeTestRule.setContent {
            AppTheme {
                MoverRow(
                    moverCoin = moverCoin(),
                    onCoinClick = { clickedCoinId = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Ethereum").performClick()

        assertThat(clickedCoinId).isEqualTo("razxDUgYGNAdQ")
    }

    private fun moverCoin(): MoverCoin =
        MoverCoin(
            id = "razxDUgYGNAdQ",
            name = "Ethereum",
            symbol = "ETH",
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
            currentPrice = Price("1875.473083380222", Currency.USD),
            priceChangePercentage24h = Percentage("-4.18"),
            sparkline = persistentListOf()
        )
}

package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import org.junit.Rule
import org.junit.Test

class GlobalMarketCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_globalMarketProvided_should_displayExpectedContent() {
        val globalMarket = globalMarket()

        composeTestRule.setContent {
            AppTheme {
                GlobalMarketCard(globalMarket = globalMarket)
            }
        }

        composeTestRule.apply {
            onNodeWithText("Total market cap").assertIsDisplayed()
            onNodeWithText(globalMarket.totalMarketCap.formattedAmount).assertIsDisplayed()
            onNodeWithText("24h volume").assertIsDisplayed()
            onNodeWithText(globalMarket.volume24h.formattedAmount).assertIsDisplayed()
            onNodeWithText(globalMarket.formattedBtcDominance).assertIsDisplayed()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithText("24h breadth").assertIsDisplayed()
            onNodeWithText("${2841.formatInteger()} up").assertIsDisplayed()
            onNodeWithText("${1893.formatInteger()} down").assertIsDisplayed()
        }
    }

    private fun globalMarket(): GlobalMarket =
        GlobalMarket(
            totalMarketCap = Price("2410000000000", Currency.USD),
            volume24h = Price("98200000000", Currency.USD),
            btcDominancePercentage = BigDecimal("54.2"),
            coinsUp24h = 2841,
            coinsDown24h = 1893
        )

    private fun Int.formatInteger(): String {
        return NumberFormat.getIntegerInstance(Locale.getDefault(Locale.Category.FORMAT))
            .format(this)
    }
}

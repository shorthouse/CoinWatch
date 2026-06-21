package dev.shorthouse.coinwatch.ui.screen.pulse

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf

@PreviewTest
@Preview
@Composable
fun PulseScreenScreenshotTest() {
    AppTheme {
        PulseScreen(
            uiState = PulseUiState(
                fearGreed = screenshotFearGreed(),
                globalMarket = screenshotGlobalMarket()
            ),
            onCoinClick = {},
            onRefresh = {},
            onDismissError = {}
        )
    }
}

private fun screenshotFearGreed() = FearGreed(
    value = 20,
    moodBand = FearGreedMoodBand.ExtremeFear,
    history = persistentListOf(
        BigDecimal("54"),
        BigDecimal("50"),
        BigDecimal("48"),
        BigDecimal("52"),
        BigDecimal("46"),
        BigDecimal("41"),
        BigDecimal("39"),
        BigDecimal("36"),
        BigDecimal("34"),
        BigDecimal("38"),
        BigDecimal("31"),
        BigDecimal("30"),
        BigDecimal("25"),
        BigDecimal("20")
    )
)

private fun screenshotGlobalMarket() = GlobalMarket(
    totalMarketCap = Price("2410000000000", Currency.USD),
    volume24h = Price("98200000000", Currency.USD),
    btcDominancePercentage = BigDecimal("54.2"),
    coinsUp24h = 2841,
    coinsDown24h = 1893
)

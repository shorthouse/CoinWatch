package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf

@PreviewTest
@Preview
@Composable
fun CoinChartCardScreenshotTest() {
    AppTheme {
        CoinChartCard(
            currentPrice = Price("1879.14"),
            coinChart = CoinChart(
                currency = Currency.USD,
                priceHistory = persistentListOf(),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Week,
            onClickChartPeriod = {}
        )
    }
}

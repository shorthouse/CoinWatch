package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.PriceEntry
import dev.shorthouse.coinwatch.ui.component.PercentageChangeChip
import dev.shorthouse.coinwatch.ui.component.ScrubPriceGraph
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import dev.shorthouse.coinwatch.ui.model.ScrubData
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

@Composable
fun CoinChartCard(
    currentPrice: Price,
    coinChart: CoinChart,
    chartPeriod: ChartPeriod,
    onClickChartPeriod: (ChartPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    var scrubData by remember { mutableStateOf<ScrubData?>(null) }

    val displayPrice = if (scrubData != null) {
        Price(
            price = scrubData!!.price.toPlainString(),
            currency = coinChart.currency,
        )
    } else {
        currentPrice
    }

    val displayPercentage = scrubData?.changePercentage ?: coinChart.periodPriceChangePercentage

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                Text(
                    text = displayPrice.formattedAmount,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    PercentageChangeChip(
                        percentage = displayPercentage,
                        modifier = Modifier.animateContentSize()
                    )

                    Spacer(Modifier.width(8.dp))

                    if (coinChart.priceHistory.isNotEmpty() && scrubData == null) {
                        Text(
                            text = stringResource(chartPeriod.longNameId),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            if (coinChart.priceHistory.isNotEmpty()) {
                ScrubPriceGraph(
                    priceHistory = coinChart.priceHistory,
                    priceChangePercentage = coinChart.periodPriceChangePercentage,
                    isGraphAnimated = true,
                    onScrub = { scrubData = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Text(
                        text = stringResource(R.string.empty_chart_message),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            ChartPeriodSelector(
                selectedChartPeriod = chartPeriod,
                onChartPeriodSelected = onClickChartPeriod,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Preview
@Composable
private fun CoinChartCardPreview() {
    var chartPeriod by remember { mutableStateOf(ChartPeriod.Day) }

    AppTheme {
        CoinChartCard(
            currentPrice = Price("1000"),
            coinChart = CoinChart(
                currency = Currency.USD,
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1737.53"), 1700021600L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1730.56"), 1700025200L, "15 Nov 2023"),
                ),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = chartPeriod,
            onClickChartPeriod = { chartPeriod = it }
        )
    }
}

@Preview
@Composable
private fun CoinChartEmptyCardPreview() {
    AppTheme {
        CoinChartCard(
            currentPrice = Price("1000"),
            coinChart = CoinChart(
                currency = Currency.USD,
                priceHistory = persistentListOf(),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Day,
            onClickChartPeriod = {}
        )
    }
}

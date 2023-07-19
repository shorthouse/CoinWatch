package dev.shorthouse.cryptodata.ui.screen.detail.component

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.shorthouse.cryptodata.ui.model.ChartPeriod
import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.ui.component.PercentageChangeChip
import dev.shorthouse.cryptodata.ui.component.PriceGraph

@Composable
fun ChartCard(
    coinDetail: CoinDetail,
    coinChart: CoinChart,
    chartPeriod: ChartPeriod,
    onClickChartPeriod: (ChartPeriod) -> Unit
) {
    Surface(shape = MaterialTheme.shapes.medium) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                Text(
                    text = coinDetail.currentPrice.formattedAmount,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    PercentageChangeChip(
                        percentage = coinChart.periodPriceChangePercentage
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = stringResource(chartPeriod.longNameId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            PriceGraph(
                prices = coinChart.prices,
                priceChangePercentage = coinChart.periodPriceChangePercentage,
                isLineAnimated = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            ChartPeriodSelector(
                selectedChartPeriod = chartPeriod,
                onChartPeriodSelected = onClickChartPeriod,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

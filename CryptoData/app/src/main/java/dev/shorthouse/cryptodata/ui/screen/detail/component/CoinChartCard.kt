package dev.shorthouse.cryptodata.ui.screen.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.ui.component.PercentageChange
import dev.shorthouse.cryptodata.ui.component.PriceGraph
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

@Composable
fun CoinChartCard(
    coinDetail: CoinDetail,
    coinChart: CoinChart,
    chartPeriod: Duration,
    onClickChartPeriod: (Duration) -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = coinDetail.currentPrice.formattedAmount,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PercentageChange(
                        percentage = coinChart.periodPriceChangePercentage
                    )
                    Text(
                        text = "Last ${chartPeriod.inWholeDays} days",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            PriceGraph(
                prices = coinChart.prices,
                priceChangePercentage = coinChart.periodPriceChangePercentage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            val chartPeriodOptions = remember {
                listOf(
                    1.days,
                    7.days,
                    30.days,
                    365.days
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                chartPeriodOptions.forEach { chartPeriodOption ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (chartPeriodOption == chartPeriod),
                                onClick = { onClickChartPeriod(chartPeriodOption) },
                                role = Role.RadioButton
                            )
                    ) {
                        RadioButton(
                            selected = (chartPeriodOption == chartPeriod),
                            onClick = null,
                            modifier = Modifier.padding(8.dp)
                        )

                        Text(
                            text = chartPeriodOption.inWholeDays.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
//
//            Row {
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(4.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = stringResource(R.string.subtitle_period_low),
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                    Text(
//                        text = coinChart.minPrice.formattedAmount,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                    PercentageChange(
//                        percentage = coinChart.minPriceChangePercentage
//                    )
//                }
//                Column(
//                    verticalArrangement = Arrangement.spacedBy(4.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = stringResource(R.string.subtitle_period_high),
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                    Text(
//                        text = coinChart.maxPrice.formattedAmount,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                    PercentageChange(
//                        percentage = coinChart.maxPriceChangePercentage
//                    )
//                }
//            }
        }
    }
}

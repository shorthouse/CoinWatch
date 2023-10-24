package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun CoinChartRangeCard(
    currentPrice: Price,
    minPrice: Price,
    maxPrice: Price,
    isPricesEmpty: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        if (!isPricesEmpty) {
            Column(modifier = Modifier.padding(12.dp)) {
                ChartRangeLine(
                    currentPrice = currentPrice,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(horizontal = 4.dp)
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = stringResource(R.string.range_title_low),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = minPrice.formattedAmount,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(R.string.range_title_high),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = maxPrice.formattedAmount,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(91.dp)
            ) {
                Text(
                    text = stringResource(R.string.empty_chart_range_message),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Preview
@Composable
fun ChartRangeCardPreview() {
    AppTheme {
        CoinChartRangeCard(
            currentPrice = Price("80.0"),
            minPrice = Price("70.0"),
            maxPrice = Price("100.0"),
            isPricesEmpty = false
        )
    }
}

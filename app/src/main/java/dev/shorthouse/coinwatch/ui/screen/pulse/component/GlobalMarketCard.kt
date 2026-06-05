package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper
import dev.shorthouse.coinwatch.ui.theme.BitcoinOrange
import dev.shorthouse.coinwatch.ui.theme.NegativeRed
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen
import java.math.BigDecimal

@Composable
fun GlobalMarketCard(
    globalMarket: GlobalMarket,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BtcDominanceRing(
                    fraction = globalMarket.btcDominanceFraction,
                    formattedPercentage = globalMarket.formattedBtcDominance
                )

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlobalMarketStat(
                        label = stringResource(R.string.pulse_total_market_cap),
                        value = globalMarket.totalMarketCap.formattedAmount,
                        valueStyle = MaterialTheme.typography.titleMedium
                    )

                    GlobalMarketStat(
                        label = stringResource(R.string.pulse_volume_24h),
                        value = globalMarket.volume24h.formattedAmount,
                        valueStyle = MaterialTheme.typography.titleMedium
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.primaryContainer)

            BreadthBar(
                coinsUpWeight = globalMarket.coinsUpWeight,
                coinsDownWeight = globalMarket.coinsDownWeight,
                formattedCoinsUp24h = globalMarket.formattedCoinsUp24h,
                formattedCoinsDown24h = globalMarket.formattedCoinsDown24h
            )
        }
    }
}

@Composable
private fun GlobalMarketStat(
    label: String,
    value: String,
    valueStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = value,
            style = valueStyle,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BtcDominanceRing(
    fraction: Float,
    formattedPercentage: String,
    modifier: Modifier = Modifier,
) {
    val trackColor = MaterialTheme.colorScheme.primaryContainer
    val sweepAngle = fraction * 360f

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            val diameter = maxOf(placeable.width, placeable.height)
            val squared = measurable.measure(Constraints.fixed(diameter, diameter))
            layout(diameter, diameter) {
                squared.place(0, 0)
            }
        }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidth = 9.dp.toPx()
            val inset = strokeWidth / 2
            val arcSize = Size(
                width = size.width - strokeWidth,
                height = size.height - strokeWidth
            )
            val topLeft = Offset(inset, inset)

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = BitcoinOrange,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(28.dp)
        ) {
            Text(
                text = formattedPercentage,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = stringResource(R.string.pulse_btc_symbol),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun BreadthBar(
    coinsUpWeight: Float,
    coinsDownWeight: Float,
    formattedCoinsUp24h: String,
    formattedCoinsDown24h: String,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(
                    R.string.pulse_breadth_up,
                    formattedCoinsUp24h
                ),
                style = MaterialTheme.typography.labelLarge,
                color = PositiveGreen,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(R.string.pulse_breadth_24h),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(
                    R.string.pulse_breadth_down,
                    formattedCoinsDown24h
                ),
                style = MaterialTheme.typography.labelLarge,
                color = NegativeRed,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(MaterialTheme.shapes.small)
        ) {
            Box(
                modifier = Modifier
                    .weight(coinsUpWeight)
                    .height(6.dp)
                    .background(PositiveGreen)
            )
            Box(
                modifier = Modifier
                    .weight(coinsDownWeight)
                    .height(6.dp)
                    .background(NegativeRed)
            )
        }
    }
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun GlobalMarketCardPreview() {
    GlobalMarketCard(
        globalMarket = GlobalMarket(
            totalMarketCap = Price("2410000000000", Currency.USD),
            volume24h = Price("98200000000", Currency.USD),
            btcDominancePercentage = BigDecimal("54.2"),
            coinsUp24h = 2841,
            coinsDown24h = 1893
        )
    )
}

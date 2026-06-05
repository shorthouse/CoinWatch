package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.TrendingCoin
import dev.shorthouse.coinwatch.ui.component.PercentageChangeChip
import dev.shorthouse.coinwatch.ui.component.StaticPriceGraph
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

@Composable
fun TrendingSpotlightCard(
    trendingCoin: TrendingCoin,
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val imageBuilder = remember(context) {
        ImageRequest.Builder(context = context)
            .decoderFactory(factory = SvgDecoder.Factory())
    }

    Surface(
        onClick = { onCoinClick(trendingCoin.id) },
        shape = MaterialTheme.shapes.large,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = imageBuilder
                        .data(trendingCoin.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = trendingCoin.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = trendingCoin.symbol,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                TrendingBadge()
            }

            StaticPriceGraph(
                prices = trendingCoin.sparkline,
                priceChangePercentage = trendingCoin.priceChangePercentage24h,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = trendingCoin.currentPrice.formattedAmount,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                PercentageChangeChip(percentage = trendingCoin.priceChangePercentage24h)
            }
        }
    }
}

@Composable
private fun TrendingBadge(
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Text(
                text = stringResource(R.string.pulse_trending_badge),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun TrendingSpotlightCardPreview() {
    TrendingSpotlightCard(
        trendingCoin = TrendingCoin(
            id = "Qwsogvtv82FCd",
            name = "Bitcoin",
            symbol = "BTC",
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29446.336548759988", Currency.USD),
            priceChangePercentage24h = Percentage("1.76833"),
            sparkline = persistentListOf(
                BigDecimal("29100"),
                BigDecimal("29250"),
                BigDecimal("29180"),
                BigDecimal("29320"),
                BigDecimal("29446")
            )
        ),
        onCoinClick = {}
    )
}

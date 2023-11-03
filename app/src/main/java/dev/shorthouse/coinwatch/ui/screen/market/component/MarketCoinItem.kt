package dev.shorthouse.coinwatch.ui.screen.market.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.ui.component.PercentageChange
import dev.shorthouse.coinwatch.ui.previewdata.CoinPreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun MarketCoinItem(
    coin: Coin,
    onCoinClick: (Coin) -> Unit,
    cardShape: Shape,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageBuilder = remember(context) {
        ImageRequest.Builder(context = context)
            .decoderFactory(factory = SvgDecoder.Factory())
    }

    Surface(
        shape = cardShape,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCoinClick(coin) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = imageBuilder
                    .data(coin.imageUrl)
                    .crossfade(true)
                    .build(),
                modifier = Modifier.size(32.dp),
                contentDescription = null
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coin.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = coin.symbol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(4.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    coin.currentPrice.formattedAmount,
                    style = MaterialTheme.typography.bodyMedium
                )

                PercentageChange(percentage = coin.priceChangePercentage24h)
            }
        }
    }
}

@Composable
@Preview
private fun MarketCoinItemPreview(
    @PreviewParameter(CoinPreviewProvider::class) coin: Coin
) {
    AppTheme {
        MarketCoinItem(
            coin = coin,
            onCoinClick = {},
            cardShape = MaterialTheme.shapes.medium
        )
    }
}

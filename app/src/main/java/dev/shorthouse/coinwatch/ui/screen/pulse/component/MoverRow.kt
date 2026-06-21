package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import dev.shorthouse.coinwatch.model.MoverCoin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.component.PercentageChange
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MoverRow(
    moverCoin: MoverCoin,
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val imageBuilder = remember(context) {
        ImageRequest.Builder(context = context)
            .decoderFactory(factory = SvgDecoder.Factory())
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCoinClick(moverCoin.id) }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        AsyncImage(
            model = imageBuilder
                .data(moverCoin.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = moverCoin.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = moverCoin.symbol,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = moverCoin.currentPrice.formattedAmount,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            PercentageChange(percentage = moverCoin.priceChangePercentage24h)
        }
    }
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun MoverRowPreview() {
    Surface(shape = MaterialTheme.shapes.medium) {
        MoverRow(
            moverCoin = MoverCoin(
                id = "razxDUgYGNAdQ",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1875.473083380222"),
                priceChangePercentage24h = Percentage("-4.18"),
                sparkline = persistentListOf()
            ),
            onCoinClick = {}
        )
    }
}

package dev.shorthouse.cryptodata.ui.screen.list.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.ui.component.PercentageChange
import dev.shorthouse.cryptodata.ui.previewdata.CoinPreviewProvider
import dev.shorthouse.cryptodata.ui.theme.AppTheme

@Composable
fun CoinListItem(
    coin: Coin,
    onCoinClick: (Coin) -> Unit,
    cardShape: Shape,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = cardShape,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCoinClick(coin) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            AsyncImage(
                model = coin.image,
                contentDescription = null,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = MaterialTheme.colorScheme.surfaceVariant,
                            backgroundColor = MaterialTheme.colorScheme.surface
                        )
                    )
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coin.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = coin.symbol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
private fun CoinListItemPreview(
    @PreviewParameter(CoinPreviewProvider::class) coin: Coin
) {
    AppTheme {
        CoinListItem(
            coin = coin,
            onCoinClick = {},
            cardShape = MaterialTheme.shapes.medium
        )
    }
}

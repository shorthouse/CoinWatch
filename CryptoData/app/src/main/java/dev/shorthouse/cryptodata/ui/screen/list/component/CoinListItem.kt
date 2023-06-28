package dev.shorthouse.cryptodata.ui.screen.list.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.ui.theme.AppTheme

@Composable
fun CoinListItem(
    coin: Coin,
    onItemClick: (Coin) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onItemClick(coin) }
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        AsyncImage(
            model = coin.image,
            contentDescription = null,
            alignment = Alignment.Center,
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = coin.name,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = coin.symbol,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = coin.priceChangePercentage24h.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                coin.currentPrice,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
private fun CoinListItemPreview() {
    AppTheme {
        CoinListItem(
            coin = Coin(
                id = "ethereum",
                symbol = "ETH",
                name = "Ethereum",
                image = "",
                currentPrice = "$1345.62",
                priceChangePercentage24h = 0.42,
                marketCapRank = 2
            ),
            onItemClick = {}
        )
    }
}

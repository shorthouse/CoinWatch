package dev.shorthouse.cryptodata.ui.screen.list.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price
import dev.shorthouse.cryptodata.ui.component.PercentageChange
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import java.math.BigDecimal

@Composable
fun CoinFavouriteItem(
    coin: Coin,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.size(180.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = coin.image,
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(Modifier.width(8.dp))

                Column {
                    Text(
                        text = coin.name,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = coin.symbol,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                coin.currentPrice.formattedAmount,
                style = MaterialTheme.typography.bodyMedium
            )

            PercentageChange(percentage = coin.priceChangePercentage24h)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CoinFavouriteItemPreview() {
    AppTheme {
        CoinFavouriteItem(
            coin = Coin(
                id = "bitcoin",
                symbol = "BTC",
                name = "Bitcoin",
                image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
                currentPrice = Price(BigDecimal("30752")),
                priceChangePercentage24h = Percentage(BigDecimal("-1.39")),
                marketCapRank = 1
            )
        )
    }
}

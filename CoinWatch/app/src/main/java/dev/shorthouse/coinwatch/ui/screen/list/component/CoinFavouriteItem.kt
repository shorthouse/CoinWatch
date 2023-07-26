package dev.shorthouse.coinwatch.ui.screen.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.component.PercentageChange
import dev.shorthouse.coinwatch.ui.component.PriceGraph
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal

@Composable
fun CoinFavouriteItem(
    coin: Coin,
    onCoinClick: (Coin) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .size(width = 140.dp, height = 200.dp)
            .clickable { onCoinClick(coin) }
    ) {
        Column {
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

            PriceGraph(
                prices = coin.prices24h,
                priceChangePercentage = coin.priceChangePercentage24h,
                isGraphAnimated = false,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
@Preview
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
                marketCapRank = 1,
                prices24h = listOf(
                    BigDecimal("30183.529754425672"), BigDecimal("30208.46501045191"), BigDecimal("30274.724311162103"), BigDecimal("30282.829639009044"), BigDecimal("30171.145399519497"), BigDecimal("30132.441662151134"), BigDecimal("30076.938233807592"), BigDecimal("29833.40434399285"), BigDecimal("29877.233983733775"), BigDecimal("29927.3342802708"), BigDecimal("30233.976977609862"), BigDecimal("30123.010990772837"), BigDecimal("30151.728192489405"), BigDecimal("30153.55063585734"), BigDecimal("30215.230717169277"), BigDecimal("30126.620956590297"), BigDecimal("30103.718108667883"), BigDecimal("30109.417062079585"), BigDecimal("30057.673289421466"), BigDecimal("29976.40770143663"), BigDecimal("30016.972147584787"), BigDecimal("30029.60386201748"), BigDecimal("29992.13845722756"), BigDecimal("30030.453838067213") // ktlint-disable max-line-length argument-list-wrapping
                )
            ),
            onCoinClick = {}
        )
    }
}

package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.MoverCoin
import dev.shorthouse.coinwatch.model.Movers
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper
import dev.shorthouse.coinwatch.ui.theme.NegativeRed
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MoversSection(
    movers: Movers,
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MoverFeatureCard(
                moverCoin = movers.topGainer,
                label = stringResource(R.string.pulse_top_gainer),
                icon = Icons.AutoMirrored.Rounded.TrendingUp,
                tint = PositiveGreen,
                onCoinClick = onCoinClick,
                modifier = Modifier.weight(1f)
            )

            MoverFeatureCard(
                moverCoin = movers.topLoser,
                label = stringResource(R.string.pulse_top_loser),
                icon = Icons.AutoMirrored.Rounded.TrendingDown,
                tint = NegativeRed,
                onCoinClick = onCoinClick,
                modifier = Modifier.weight(1f)
            )
        }

        if (movers.mostMovement.isNotEmpty()) {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.pulse_most_movement).uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )

                    movers.mostMovement.forEachIndexed { index, moverCoin ->
                        MoverRow(
                            moverCoin = moverCoin,
                            onCoinClick = onCoinClick
                        )

                        if (index != movers.mostMovement.lastIndex) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.background)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun MoversSectionPreview() {
    MoversSection(
        movers = Movers(
            topGainer = MoverCoin(
                id = "gainer",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29446.33"),
                priceChangePercentage24h = Percentage("12.34"),
                sparkline = persistentListOf()
            ),
            topLoser = MoverCoin(
                id = "loser",
                name = "Dogecoin",
                symbol = "DOGE",
                imageUrl = "https://cdn.coinranking.com/H1arVnjQ_/doge.svg",
                currentPrice = Price("0.11923"),
                priceChangePercentage24h = Percentage("-9.87"),
                sparkline = persistentListOf()
            ),
            mostMovement = persistentListOf(
                MoverCoin(
                    id = "eth",
                    name = "Ethereum",
                    symbol = "ETH",
                    imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                    currentPrice = Price("1875.47"),
                    priceChangePercentage24h = Percentage("-7.21"),
                    sparkline = persistentListOf()
                ),
                MoverCoin(
                    id = "algo",
                    name = "Algorand",
                    symbol = "ALGO",
                    imageUrl = "https://cdn.coinranking.com/fztgfHckp/algorand-algo-logo.svg",
                    currentPrice = Price("0.18234"),
                    priceChangePercentage24h = Percentage("6.42"),
                    sparkline = persistentListOf()
                )
            )
        ),
        onCoinClick = {}
    )
}

package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun MarketStatsCard(
    coinDetails: CoinDetails,
    modifier: Modifier = Modifier
) {
    val coinDetailsItems = remember(coinDetails) {
        listOf(
            CoinDetailsListItem(
                nameId = R.string.list_item_market_cap_rank,
                value = coinDetails.marketCapRank
            ),
            CoinDetailsListItem(
                nameId = R.string.list_item_market_cap,
                value = coinDetails.marketCap.formattedAmount
            ),
            CoinDetailsListItem(
                nameId = R.string.list_item_volume_24h,
                value = coinDetails.volume24h
            ),
            CoinDetailsListItem(
                nameId = R.string.list_item_circulating_supply,
                value = coinDetails.circulatingSupply
            ),
            CoinDetailsListItem(
                nameId = R.string.list_item_ath,
                value = coinDetails.allTimeHigh.formattedAmount
            ),
            CoinDetailsListItem(
                nameId = R.string.list_item_ath_date,
                value = coinDetails.allTimeHighDate
            ),
            CoinDetailsListItem(
                nameId = R.string.list_item_listed_date,
                value = coinDetails.listedDate
            )
        )
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                coinDetailsItems.forEachIndexed { coinDetailsIndex, coinDetailsListItem ->
                    if (coinDetailsIndex != 0) {
                        Divider(color = MaterialTheme.colorScheme.background)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(coinDetailsListItem.nameId),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = coinDetailsListItem.value,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

private data class CoinDetailsListItem(
    @StringRes val nameId: Int,
    val value: String
)

@Preview
@Composable
private fun MarketStatsCardPreview() {
    AppTheme {
        MarketStatsCard(
            coinDetails = CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                marketCapRank = "2",
                volume24h = "6,627,669,115",
                circulatingSupply = "120,186,525",
                allTimeHigh = Price("4878.26"),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            )

        )
    }
}

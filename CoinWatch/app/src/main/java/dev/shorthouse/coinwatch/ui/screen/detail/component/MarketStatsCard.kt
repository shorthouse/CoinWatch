package dev.shorthouse.coinwatch.ui.screen.detail.component

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
import dev.shorthouse.coinwatch.model.CoinDetail
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import java.math.BigDecimal

@Composable
fun MarketStatsCard(
    coinDetail: CoinDetail,
    modifier: Modifier = Modifier
) {
    val coinDetailItems = remember(coinDetail) {
        listOf(
            CoinDetailListItem(
                nameId = R.string.list_item_market_cap_rank,
                value = coinDetail.marketCapRank
            ),
            CoinDetailListItem(
                nameId = R.string.list_item_market_cap,
                value = coinDetail.marketCap.formattedAmount
            ),
            CoinDetailListItem(
                nameId = R.string.list_item_volume_24h,
                value = coinDetail.volume24h
            ),
            CoinDetailListItem(
                nameId = R.string.list_item_circulating_supply,
                value = coinDetail.circulatingSupply
            ),
            CoinDetailListItem(
                nameId = R.string.list_item_ath,
                value = coinDetail.allTimeHigh.formattedAmount
            ),
            CoinDetailListItem(
                nameId = R.string.list_item_ath_date,
                value = coinDetail.allTimeHighDate
            ),
            CoinDetailListItem(
                nameId = R.string.list_item_listed_date,
                value = coinDetail.listedDate
            )
        )
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                coinDetailItems.forEachIndexed { coinDetailIndex, coinDetailListItem ->
                    if (coinDetailIndex != 0) {
                        Divider(color = MaterialTheme.colorScheme.background)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(coinDetailListItem.nameId),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = coinDetailListItem.value,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

private data class CoinDetailListItem(
    @StringRes val nameId: Int,
    val value: String
)

@Preview
@Composable
private fun MarketStatsCardPreview() {
    AppTheme {
        MarketStatsCard(
            coinDetail = CoinDetail(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                image = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price(BigDecimal("1879.14")),
                marketCap = Price(BigDecimal("225722901094")),
                marketCapRank = "2",
                volume24h = "6,627,669,115",
                circulatingSupply = "120,186,525",
                allTimeHigh = Price(BigDecimal("4878.26")),
                allTimeHighDate = "10 Nov 2021",
                listedDate = "7 Aug 2015"
            )
        )
    }
}

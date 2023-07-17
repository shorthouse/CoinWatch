package dev.shorthouse.cryptodata.ui.screen.detail.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.model.CoinDetail

@Composable
fun MarketStatsCard(coinDetail: CoinDetail) {
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
                nameId = R.string.list_item_atl,
                value = coinDetail.allTimeLow.formattedAmount
            ),
            CoinDetailListItem(
                nameId = R.string.list_item_atl_date,
                value = coinDetail.allTimeLowDate
            )
        )
    }

    Surface(shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(R.string.card_header_market_stats),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

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
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

data class CoinDetailListItem(
    @StringRes val nameId: Int,
    val value: String
)

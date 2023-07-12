package dev.shorthouse.cryptodata.ui.screen.detail.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.model.CoinDetail

@Composable
fun CoinDetailCard(
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

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.card_header_details),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                coinDetailItems.forEach { coinDetailListItem ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(coinDetailListItem.nameId),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = coinDetailListItem.value,
                            style = MaterialTheme.typography.bodyLarge
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

// @Composable
// @Preview(showBackground = true)
// private fun CoinDetailListPreview() {
//    AppTheme {
//        CoinDetailCard(
//
//    }
// }

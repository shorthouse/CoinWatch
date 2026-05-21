package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import dev.shorthouse.coinwatch.model.Price
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@Composable
fun MarketStatsCard(
    coinDetails: CoinDetails,
    modifier: Modifier = Modifier,
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
                nameId = R.string.list_item_fully_diluted_market_cap,
                value = coinDetails.fullyDilutedMarketCap.formattedAmount
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
                nameId = R.string.list_item_volume_24h,
                value = coinDetails.volume24h.formattedAmount
            ),
            CoinDetailsListItem(
                nameId = R.string.list_item_exchanges,
                value = coinDetails.numberOfExchanges
            ),
            CoinDetailsListItem(
                nameId = R.string.list_item_markets,
                value = coinDetails.numberOfMarkets
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
                        HorizontalDivider(color = MaterialTheme.colorScheme.primaryContainer)
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(coinDetailsListItem.nameId),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                        )

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = coinDetailsListItem.value,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

private data class CoinDetailsListItem(
    @StringRes val nameId: Int,
    val value: String,
)

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun MarketStatsCardDollarPreview() {
    MarketStatsCard(
        coinDetails = CoinDetails(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            description = "Ethereum is a decentralized blockchain with smart contract functionality.",
            tags = persistentListOf("smart-contracts", "staking"),
            links = persistentListOf(
                CoinLink(
                    type = CoinLinkType.Website,
                    url = "https://ethereum.org",
                )
            ),
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
            currentPrice = Price("1879.14"),
            marketCap = Price("225722901094"),
            fullyDilutedMarketCap = Price("225722901094"),
            marketCapRank = "2",
            volume24h = Price("6,627,669,115"),
            numberOfExchanges = "248",
            numberOfMarkets = "1,098",
            circulatingSupply = "120,186,525",
            totalSupply = "120,500,000",
            maxSupply = "210,000,000",
            allTimeHigh = Price("4878.26"),
            allTimeHighDate = "10 Nov 2021",
            listedDate = "7 Aug 2015"
        )
    )

}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun MarketStatsCardGBPPreview() {
    MarketStatsCard(
        coinDetails = CoinDetails(
            id = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            description = "Ethereum is a decentralized blockchain with smart contract functionality.",
            tags = persistentListOf("smart-contracts", "staking"),
            links = persistentListOf(
                CoinLink(
                    type = CoinLinkType.Website,
                    url = "https://ethereum.org",
                )
            ),
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
            currentPrice = Price("1879.14", currency = Currency.GBP),
            marketCap = Price("225722901094", currency = Currency.GBP),
            fullyDilutedMarketCap = Price("225722901094", currency = Currency.GBP),
            marketCapRank = "2",
            volume24h = Price("6,627,669,115", currency = Currency.GBP),
            numberOfExchanges = "248",
            numberOfMarkets = "1,098",
            circulatingSupply = "120,186,525",
            totalSupply = "120,500,000",
            maxSupply = "210,000,000",
            allTimeHigh = Price("4878.26", currency = Currency.GBP),
            allTimeHighDate = "10 Nov 2021",
            listedDate = "7 Aug 2015"
        )
    )

}

package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf

@PreviewTest
@Preview
@Composable
fun MarketStatsCardScreenshotTest() {
    AppTheme {
        MarketStatsCard(
            coinDetails = CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                description = "Ethereum is a decentralized blockchain with smart contract functionality.",
                tags = persistentListOf("smart-contracts", "staking", "layer-1"),
                links = persistentListOf(
                    CoinLink(
                        type = CoinLinkType.Website,
                        url = "https://ethereum.org",
                    )
                ),
                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                currentPrice = Price("1879.14"),
                marketCap = Price("225722901094"),
                fullyDilutedMarketCap = Price("250000000000"),
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
}

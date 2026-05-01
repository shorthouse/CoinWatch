package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.PriceEntry
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import dev.shorthouse.coinwatch.ui.screen.details.DetailsUiState
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

class DetailsUiStatePreviewProvider : PreviewParameterProvider<DetailsUiState> {
    override val values = sequenceOf(
        DetailsUiState.Success(
            CoinDetails(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                description = "Ethereum is a decentralized blockchain with smart contract functionality.",
                tags = persistentListOf("smart-contracts", "staking", "layer-1"),
                links = persistentListOf(
                    CoinLink(
                        type = CoinLinkType.Website,
                        url = "https://ethereum.org",
                    ),
                    CoinLink(
                        type = CoinLinkType.GitHub,
                        url = "https://github.com/ethereum",
                    ),
                    CoinLink(
                        type = CoinLinkType.Reddit,
                        url = "https://reddit.com/r/ethereum",
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
            ),
            CoinChart(
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("1755.19"), 1700000000L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1749.71"), 1700003600L, "14 Nov 2023"),
                    PriceEntry(BigDecimal("1750.94"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1748.44"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.98"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.25"), 1700018000L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1737.53"), 1700021600L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1730.56"), 1700025200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1738.12"), 1700028800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1736.10"), 1700032400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.20"), 1700036000L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.64"), 1700039600L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1741.49"), 1700043200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1738.87"), 1700046800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1734.92"), 1700050400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1736.79"), 1700054000L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.53"), 1700057600L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.21"), 1700061200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1744.75"), 1700064800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1744.85"), 1700068400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1741.76"), 1700072000L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1741.46"), 1700075600L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1739.82"), 1700079200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1740.15"), 1700082800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1745.08"), 1700086400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1743.29"), 1700090000L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("1746.12"), 1700093600L, "16 Nov 2023"),
                    PriceEntry(BigDecimal("1745.99"), 1700097200L, "16 Nov 2023"),
                    PriceEntry(BigDecimal("1744.89"), 1700100800L, "16 Nov 2023"),
                    PriceEntry(BigDecimal("1741.10"), 1700104400L, "16 Nov 2023"),
                    PriceEntry(BigDecimal("1738.36"), 1700108000L, "16 Nov 2023"),
                ),
                minPrice = Price("1632.46"),
                maxPrice = Price("1922.83"),
                periodPriceChangePercentage = Percentage("7.06")
            ),
            chartPeriod = ChartPeriod.Week,
            isCoinFavourite = true
        ),
        DetailsUiState.Error("No internet connection"),
        DetailsUiState.Loading
    )
}

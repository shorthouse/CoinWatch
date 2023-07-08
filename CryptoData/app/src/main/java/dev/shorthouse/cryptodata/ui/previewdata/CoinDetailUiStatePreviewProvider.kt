package dev.shorthouse.cryptodata.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price
import dev.shorthouse.cryptodata.ui.screen.detail.CoinDetailUiState
import kotlin.time.Duration.Companion.days

class CoinDetailUiStatePreviewProvider : PreviewParameterProvider<CoinDetailUiState> {
    override val values = sequenceOf(
        CoinDetailUiState.Success(
            CoinDetail(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                image = " https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
                currentPrice = Price(1879.14),
                marketCapRank = "2",
                marketCap = Price(225722901094),
                circulatingSupply = "120,186,525",
                allTimeLow = Price(0.43),
                allTimeHigh = Price(4878.26),
                allTimeLowDate = "20 Oct 2015",
                allTimeHighDate = "10 Nov 2021"
            ),
            CoinChart(
                prices = listOf(
                    1755.19, 1749.71, 1750.94, 1748.44, 1743.98, 1740.25, 1737.53, 1730.56, 1738.12, 1736.10, 1740.20, 1740.64, 1741.49, 1738.87, 1734.92, 1736.79, 1743.53, 1743.21, 1744.75, 1744.85, 1741.76, 1741.46, 1739.82, 1740.15, 1745.08, 1743.29, 1746.12, 1745.99, 1744.89, 1741.10, 1741.91, 1738.47, 1737.67, 1741.82, 1735.95, 1728.11, 1657.23, 1649.89, 1649.71, 1650.68, 1654.04, 1648.55, 1650.10, 1651.87, 1651.29, 1642.75, 1637.79, 1635.80, 1637.01, 1632.46, 1633.31, 1640.08, 1638.61, 1645.47, 1643.50, 1640.57, 1640.41, 1641.38, 1660.21, 1665.73, 1660.33, 1665.65, 1664.11, 1665.71, 1661.90, 1661.17, 1662.54, 1665.58, 1666.27, 1669.82, 1671.34, 1669.87, 1670.62, 1668.97, 1668.86, 1664.58, 1665.96, 1664.53, 1656.15, 1670.91, 1685.59, 1693.69, 1718.10, 1719.56, 1724.42, 1717.22, 1718.34, 1716.38, 1715.37, 1716.46, 1719.39, 1717.94, 1722.92, 1755.97, 1749.11, 1742.58, 1742.88, 1743.36, 1742.95, 1739.68, 1736.65, 1739.88, 1734.35, 1727.31, 1728.35, 1724.05, 1730.04, 1726.87, 1727.71, 1728.49, 1729.93, 1726.37, 1722.92, 1726.67, 1724.76, 1728.41, 1729.20, 1728.20, 1727.98, 1729.96, 1727.80, 1732.04, 1730.22, 1733.16, 1734.14, 1734.31, 1739.62, 1737.76, 1739.52, 1742.98, 1738.36
                ),
                minPrice = Price(1632.46),
                minPriceChangePercentage = Percentage(15.11),
                maxPrice = Price(1922.83),
                maxPriceChangePercentage = Percentage(-2.27),
                periodPriceChangePercentage = Percentage(7.06)
            ),
            chartPeriod = 7.days
        ),
        CoinDetailUiState.Error("Error message"),
        CoinDetailUiState.Loading,
    )
}

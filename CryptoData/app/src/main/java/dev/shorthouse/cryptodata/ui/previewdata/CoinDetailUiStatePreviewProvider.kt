package dev.shorthouse.cryptodata.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.cryptodata.ui.model.ChartPeriod
import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price
import dev.shorthouse.cryptodata.ui.screen.detail.CoinDetailUiState
import java.math.BigDecimal

class CoinDetailUiStatePreviewProvider : PreviewParameterProvider<CoinDetailUiState> {
    override val values = sequenceOf(
        CoinDetailUiState.Success(
            CoinDetail(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                image = "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
                currentPrice = Price(BigDecimal("1879.14")),
                marketCapRank = "2",
                marketCap = Price(BigDecimal("225722901094")),
                circulatingSupply = "120,186,525",
                allTimeLow = Price(BigDecimal("0.43")),
                allTimeHigh = Price(BigDecimal("4878.26")),
                allTimeLowDate = "20 Oct 2015",
                allTimeHighDate = "10 Nov 2021"
            ),
            CoinChart(
                prices = listOf(
                    BigDecimal("1755.19"), BigDecimal("1749.71"), BigDecimal("1750.94"), BigDecimal("1748.44"), BigDecimal("1743.98"), BigDecimal("1740.25"), BigDecimal("1737.53"), BigDecimal("1730.56"), BigDecimal("1738.12"), BigDecimal("1736.10"), BigDecimal("1740.20"), BigDecimal("1740.64"), BigDecimal("1741.49"), BigDecimal("1738.87"), BigDecimal("1734.92"), BigDecimal("1736.79"), BigDecimal("1743.53"), BigDecimal("1743.21"), BigDecimal("1744.75"), BigDecimal("1744.85"), BigDecimal("1741.76"), BigDecimal("1741.46"), BigDecimal("1739.82"), BigDecimal("1740.15"), BigDecimal("1745.08"), BigDecimal("1743.29"), BigDecimal("1746.12"), BigDecimal("1745.99"), BigDecimal("1744.89"), BigDecimal("1741.10"), BigDecimal("1741.91"), BigDecimal("1738.47"), BigDecimal("1737.67"), BigDecimal("1741.82"), BigDecimal("1735.95"), BigDecimal("1728.11"), BigDecimal("1657.23"), BigDecimal("1649.89"), BigDecimal("1649.71"), BigDecimal("1650.68"), BigDecimal("1654.04"), BigDecimal("1648.55"), BigDecimal("1650.10"), BigDecimal("1651.87"), BigDecimal("1651.29"), BigDecimal("1642.75"), BigDecimal("1637.79"), BigDecimal("1635.80"), BigDecimal("1637.01"), BigDecimal("1632.46"), BigDecimal("1633.31"), BigDecimal("1640.08"), BigDecimal("1638.61"), BigDecimal("1645.47"), BigDecimal("1643.50"), BigDecimal("1640.57"), BigDecimal("1640.41"), BigDecimal("1641.38"), BigDecimal("1660.21"), BigDecimal("1665.73"), BigDecimal("1660.33"), BigDecimal("1665.65"), BigDecimal("1664.11"), BigDecimal("1665.71"), BigDecimal("1661.90"), BigDecimal("1661.17"), BigDecimal("1662.54"), BigDecimal("1665.58"), BigDecimal("1666.27"), BigDecimal("1669.82"), BigDecimal("1671.34"), BigDecimal("1669.87"), BigDecimal("1670.62"), BigDecimal("1668.97"), BigDecimal("1668.86"), BigDecimal("1664.58"), BigDecimal("1665.96"), BigDecimal("1664.53"), BigDecimal("1656.15"), BigDecimal("1670.91"), BigDecimal("1685.59"), BigDecimal("1693.69"), BigDecimal("1718.10"), BigDecimal("1719.56"), BigDecimal("1724.42"), BigDecimal("1717.22"), BigDecimal("1718.34"), BigDecimal("1716.38"), BigDecimal("1715.37"), BigDecimal("1716.46"), BigDecimal("1719.39"), BigDecimal("1717.94"), BigDecimal("1722.92"), BigDecimal("1755.97"), BigDecimal("1749.11"), BigDecimal("1742.58"), BigDecimal("1742.88"), BigDecimal("1743.36"), BigDecimal("1742.95"), BigDecimal("1739.68"), BigDecimal("1736.65"), BigDecimal("1739.88"), BigDecimal("1734.35"), BigDecimal("1727.31"), BigDecimal("1728.35"), BigDecimal("1724.05"), BigDecimal("1730.04"), BigDecimal("1726.87"), BigDecimal("1727.71"), BigDecimal("1728.49"), BigDecimal("1729.93"), BigDecimal("1726.37"), BigDecimal("1722.92"), BigDecimal("1726.67"), BigDecimal("1724.76"), BigDecimal("1728.41"), BigDecimal("1729.20"), BigDecimal("1728.20"), BigDecimal("1727.98"), BigDecimal("1729.96"), BigDecimal("1727.80"), BigDecimal("1732.04"), BigDecimal("1730.22"), BigDecimal("1733.16"), BigDecimal("1734.14"), BigDecimal("1734.31"), BigDecimal("1739.62"), BigDecimal("1737.76"), BigDecimal("1739.52"), BigDecimal("1742.98"), BigDecimal("1738.36") // ktlint-disable argument-list-wrapping
                ),
                minPrice = Price(BigDecimal("1632.46")),
                minPriceChangePercentage = Percentage(BigDecimal("15.11")),
                maxPrice = Price(BigDecimal("1922.83")),
                maxPriceChangePercentage = Percentage(BigDecimal("-2.27")),
                periodPriceChangePercentage = Percentage(BigDecimal("7.06")),
            ),
            chartPeriod = ChartPeriod.Week
        ),
        CoinDetailUiState.Error("Error message"),
        CoinDetailUiState.Loading
    )
}

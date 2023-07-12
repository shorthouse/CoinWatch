package dev.shorthouse.cryptodata.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price
import dev.shorthouse.cryptodata.ui.screen.list.CoinListUiState
import java.math.BigDecimal

class CoinListUiStatePreviewProvider : PreviewParameterProvider<CoinListUiState> {
    override val values = sequenceOf(
        CoinListUiState.Success(
            coins = listOf(
                Coin(
                    id = "bitcoin",
                    symbol = "BTC",
                    name = "Bitcoin",
                    image = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579",
                    currentPrice = Price(BigDecimal("30752")),
                    priceChangePercentage24h = Percentage(BigDecimal("-1.39")),
                    marketCapRank = 1
                ),
                Coin(
                    id = "ethereum",
                    symbol = "ETH",
                    name = "Ethereum",
                    image = "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
                    currentPrice = Price(BigDecimal("1345.62")),
                    priceChangePercentage24h = Percentage(BigDecimal("0.42")),
                    marketCapRank = 2
                ),
                Coin(
                    id = "tether",
                    symbol = "USDT",
                    name = "Tether",
                    image = "https://assets.coingecko.com/coins/images/325/large/Tether.png?1668148663",
                    currentPrice = Price(BigDecimal("1.0")),
                    priceChangePercentage24h = Percentage(BigDecimal("0.00")),
                    marketCapRank = 3
                )
            )
        ),
        CoinListUiState.Error("Error message"),
        CoinListUiState.Loading
    )
}

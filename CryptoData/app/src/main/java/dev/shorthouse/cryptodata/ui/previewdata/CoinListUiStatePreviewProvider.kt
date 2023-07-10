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
                    id = "ethereum",
                    symbol = "ETH",
                    name = "Ethereum",
                    image = "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
                    currentPrice = Price(BigDecimal("1345.62")),
                    priceChangePercentage24h = Percentage(BigDecimal("0.42")),
                    marketCapRank = 2
                )
            )
        ),
        CoinListUiState.Error("Error message"),
        CoinListUiState.Loading
    )
}

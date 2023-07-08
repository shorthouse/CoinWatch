package dev.shorthouse.cryptodata.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price

class CoinPreviewProvider : PreviewParameterProvider<Coin> {
    override val values = sequenceOf(
        Coin(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            image = "",
            currentPrice = Price(26321.14),
            priceChangePercentage24h = Percentage(-1.12),
            marketCapRank = 1
        ),
        Coin(
            id = "ethereum",
            symbol = "ETH",
            name = "Ethereum",
            image = "",
            currentPrice = Price(1345.62),
            priceChangePercentage24h = Percentage(0.42),
            marketCapRank = 2
        ),
        Coin(
            id = "tether",
            symbol = "USDT",
            name = "Tether",
            image = "",
            currentPrice = Price(1.0),
            priceChangePercentage24h = Percentage(0.0),
            marketCapRank = 3
        ),
    )
}

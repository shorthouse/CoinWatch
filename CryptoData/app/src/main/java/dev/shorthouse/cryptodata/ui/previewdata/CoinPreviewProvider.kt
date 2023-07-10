package dev.shorthouse.cryptodata.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price
import java.math.BigDecimal

class CoinPreviewProvider : PreviewParameterProvider<Coin> {
    override val values = sequenceOf(
        Coin(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            image = "",
            currentPrice = Price(BigDecimal("26321.14")),
            priceChangePercentage24h = Percentage(BigDecimal("-1.12")),
            marketCapRank = 1
        ),
        Coin(
            id = "ethereum",
            symbol = "ETH",
            name = "Ethereum",
            image = "",
            currentPrice = Price(BigDecimal("1345.62")),
            priceChangePercentage24h = Percentage(BigDecimal("0.42")),
            marketCapRank = 2
        ),
        Coin(
            id = "tether",
            symbol = "USDT",
            name = "Tether",
            image = "",
            currentPrice = Price(BigDecimal("1.00")),
            priceChangePercentage24h = Percentage(BigDecimal("0.0")),
            marketCapRank = 3
        )
    )
}

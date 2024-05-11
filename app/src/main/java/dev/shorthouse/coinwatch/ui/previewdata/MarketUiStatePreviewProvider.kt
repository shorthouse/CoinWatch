package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.data.source.local.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import dev.shorthouse.coinwatch.ui.previewdata.CoinListPreviewData.coins
import dev.shorthouse.coinwatch.ui.screen.market.MarketUiState
import kotlinx.collections.immutable.persistentListOf

class MarketUiStatePreviewProvider : PreviewParameterProvider<MarketUiState> {
    override val values = sequenceOf(
        MarketUiState(
            coins = coins,
            timeOfDay = TimeOfDay.Evening,
            marketCapChangePercentage24h = Percentage("1.3")
        ),
        MarketUiState(
            coins = persistentListOf()
        ),
        MarketUiState(
            isLoading = true
        )
    )
}

private object CoinListPreviewData {
    val coins = persistentListOf(
        Coin(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29446.336548759988"),
            priceChangePercentage24h = Percentage("0.76833"),
        ),
        Coin(
            id = "ethereum",
            symbol = "ETH",
            name = "Ethereum",
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
            currentPrice = Price("1875.473083380222"),
            priceChangePercentage24h = Percentage("-1.11008"),
        ),
        Coin(
            id = "tether",
            symbol = "USDT",
            name = "Tether USD",
            imageUrl = "https://cdn.coinranking.com/mgHqwlCLj/usdt.svg",
            currentPrice = Price("1.00"),
            priceChangePercentage24h = Percentage("0.00"),
        ),
        Coin(
            id = "binancecoin",
            symbol = "BNB",
            name = "BNB",
            imageUrl = "https://cdn.coinranking.com/B1N19L_dZ/bnb.svg",
            currentPrice = Price("242.13321783678734"),
            priceChangePercentage24h = Percentage("1.84955"),
        ),
        Coin(
            id = "ripple",
            symbol = "XRP",
            name = "XRP",
            imageUrl = "https://cdn.coinranking.com/B1oPuTyfX/xrp.svg",
            currentPrice = Price("0.7142802333064954"),
            priceChangePercentage24h = Percentage("1.77031"),
        ),
        Coin(
            id = "usdc",
            symbol = "USDC",
            name = "USDC",
            imageUrl = "https://cdn.coinranking.com/jkDf8sQbY/usdc.svg",
            currentPrice = Price("1.00"),
            priceChangePercentage24h = Percentage("0.00"),
        ),
        Coin(
            id = "polkadot",
            symbol = "DOT",
            name = "Polkadot",
            imageUrl = "https://cdn.coinranking.com/V3NSSybv-/polkadot-dot.svg",
            currentPrice = Price("4.422860504529326"),
            priceChangePercentage24h = Percentage("-0.44"),
        ),
        Coin(
            id = "solana",
            symbol = "SOL",
            name = "Solana",
            imageUrl = "https://cdn.coinranking.com/yvUG4Qex5/solana.svg",
            currentPrice = Price("50.99668115384087"),
            priceChangePercentage24h = Percentage("2.45"),
        ),
        Coin(
            id = "dogecoin",
            symbol = "DOGE",
            name = "Dogecoin",
            imageUrl = "https://cdn.coinranking.com/H1arXIuOZ/doge.svg",
            currentPrice = Price("0.07353603881046378"),
            priceChangePercentage24h = Percentage("-3.34"),
        ),
        Coin(
            id = "tron",
            symbol = "TRX",
            name = "TRON",
            imageUrl = "https://cdn.coinranking.com/behejNqQs/trx.svg",
            currentPrice = Price("0.10538265070088429"),
            priceChangePercentage24h = Percentage("1.99"),
        ),
        Coin(
            id = "chainlink",
            symbol = "LINK",
            name = "Chainlink",
            imageUrl = "https://cdn.coinranking.com/9NOP9tOem/chainlink.svg",
            currentPrice = Price("15.102872334646245"),
            priceChangePercentage24h = Percentage("3.28"),
        ),
        Coin(
            id = "polygon",
            symbol = "MATIC",
            name = "Polygon",
            imageUrl = "https://cdn.coinranking.com/M-pwilaq-/polygon-matic-logo.svg",
            currentPrice = Price("0.8245413634145558"),
            priceChangePercentage24h = Percentage("1.44"),
        )
    )
}

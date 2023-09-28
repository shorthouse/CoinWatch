package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.SearchCoin

class SearchCoinPreviewProvider : PreviewParameterProvider<SearchCoin> {
    override val values = sequenceOf(
        SearchCoin(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29446.336548759988")
        ),
        SearchCoin(
            id = "ethereum",
            symbol = "ETH",
            name = "Ethereum",
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
            currentPrice = Price("1875.473083380222")
        ),
        SearchCoin(
            id = "tether",
            symbol = "USDT",
            name = "Tether",
            imageUrl = "https://cdn.coinranking.com/mgHqwlCLj/usdt.svg",
            currentPrice = Price("1.00")
        )
    )
}

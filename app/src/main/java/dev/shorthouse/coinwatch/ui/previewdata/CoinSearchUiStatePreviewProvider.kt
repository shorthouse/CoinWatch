package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.SearchCoin
import dev.shorthouse.coinwatch.ui.previewdata.CoinSearchPreviewData.searchResults
import dev.shorthouse.coinwatch.ui.screen.search.CoinSearchUiState
import kotlinx.collections.immutable.persistentListOf

class CoinSearchUiStatePreviewProvider : PreviewParameterProvider<CoinSearchUiState> {
    override val values = sequenceOf(
        CoinSearchUiState.Success(
            searchResults = searchResults,
            queryHasNoResults = false
        ),
        CoinSearchUiState.Loading,
        CoinSearchUiState.Error(
            message = "Error searching coins"
        )
    )
}

private object CoinSearchPreviewData {
    val searchResults = persistentListOf(
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

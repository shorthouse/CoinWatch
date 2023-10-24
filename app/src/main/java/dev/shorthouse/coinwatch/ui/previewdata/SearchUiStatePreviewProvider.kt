package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.model.SearchCoin
import dev.shorthouse.coinwatch.ui.previewdata.CoinSearchPreviewData.searchResults
import dev.shorthouse.coinwatch.ui.screen.search.SearchUiState
import kotlinx.collections.immutable.persistentListOf

class SearchUiStatePreviewProvider : PreviewParameterProvider<SearchUiState> {
    override val values = sequenceOf(
        SearchUiState.Success(
            searchResults = searchResults,
            queryHasNoResults = false
        ),
        SearchUiState.Success(
            searchResults = persistentListOf(),
            queryHasNoResults = true
        ),
        SearchUiState.Loading,
        SearchUiState.Error(
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
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg"
        ),
        SearchCoin(
            id = "ethereum",
            symbol = "ETH",
            name = "Ethereum",
            imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg"
        ),
        SearchCoin(
            id = "tether",
            symbol = "USDT",
            name = "Tether",
            imageUrl = "https://cdn.coinranking.com/mgHqwlCLj/usdt.svg"
        )
    )
}

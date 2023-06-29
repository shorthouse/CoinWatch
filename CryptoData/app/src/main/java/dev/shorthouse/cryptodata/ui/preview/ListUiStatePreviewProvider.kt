package dev.shorthouse.cryptodata.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.ui.screen.list.ListUiState

class ListUiStatePreviewProvider : PreviewParameterProvider<ListUiState> {
    override val values = sequenceOf(
        ListUiState.Success(
            coins = listOf(
                Coin(
                    id = "ethereum",
                    symbol = "ETH",
                    name = "Ethereum",
                    image = "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",
                    currentPrice = "$1345.62",
                    priceChangePercentage24h = 0.42,
                    marketCapRank = 2
                )
            )
        )
    )
}

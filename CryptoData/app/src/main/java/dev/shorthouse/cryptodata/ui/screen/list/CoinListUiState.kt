package dev.shorthouse.cryptodata.ui.screen.list

import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.MarketStats
import dev.shorthouse.cryptodata.ui.model.TimeOfDay

sealed interface CoinListUiState {
    object Loading : CoinListUiState
    data class Success(
        val coins: List<Coin>,
        val favouriteCoins: List<Coin>,
        val marketStats: MarketStats,
        val timeOfDay: TimeOfDay
    ) : CoinListUiState
    data class Error(val message: String?) : CoinListUiState
}

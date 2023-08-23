package dev.shorthouse.coinwatch.ui.screen.list

import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.ui.model.TimeOfDay

sealed interface CoinListUiState {
    object Loading : CoinListUiState
    data class Success(
        val coins: List<Coin>,
        val favouriteCoins: List<Coin>,
        val timeOfDay: TimeOfDay
    ) : CoinListUiState
    data class Error(val message: String?) : CoinListUiState
}

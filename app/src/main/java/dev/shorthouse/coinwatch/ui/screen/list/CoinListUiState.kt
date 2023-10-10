package dev.shorthouse.coinwatch.ui.screen.list

import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import kotlinx.collections.immutable.ImmutableList

sealed interface CoinListUiState {
    object Loading : CoinListUiState
    data class Success(
        val coins: ImmutableList<Coin>,
        val favouriteCoins: ImmutableList<Coin>,
        val timeOfDay: TimeOfDay
    ) : CoinListUiState

    data class Error(val message: String?) : CoinListUiState
}

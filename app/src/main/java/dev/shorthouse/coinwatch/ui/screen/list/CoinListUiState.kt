package dev.shorthouse.coinwatch.ui.screen.list

import dev.shorthouse.coinwatch.model.Coin
import kotlinx.collections.immutable.ImmutableList

sealed interface CoinListUiState {
    data class Success(
        val coins: ImmutableList<Coin>
    ) : CoinListUiState

    data class Error(val message: String?) : CoinListUiState
    object Loading : CoinListUiState
}

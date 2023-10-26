package dev.shorthouse.coinwatch.ui.screen.list

import dev.shorthouse.coinwatch.model.Coin
import kotlinx.collections.immutable.ImmutableList

sealed interface ListUiState {
    data class Success(
        val coins: ImmutableList<Coin>
    ) : ListUiState

    data class Error(val message: String?) : ListUiState
    object Loading : ListUiState
}

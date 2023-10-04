package dev.shorthouse.coinwatch.ui.screen.search

import dev.shorthouse.coinwatch.model.SearchCoin
import kotlinx.collections.immutable.ImmutableList

sealed interface CoinSearchUiState {
    data object Loading : CoinSearchUiState
    data class Success(
        val searchResults: ImmutableList<SearchCoin>,
        val queryHasNoResults: Boolean
    ) : CoinSearchUiState
    data class Error(val message: String?) : CoinSearchUiState
}

package dev.shorthouse.coinwatch.ui.screen.search

import dev.shorthouse.coinwatch.model.SearchCoin
import kotlinx.collections.immutable.ImmutableList

sealed interface SearchUiState {
    object Loading : SearchUiState
    data class Success(
        val searchResults: ImmutableList<SearchCoin>,
        val queryHasNoResults: Boolean
    ) : SearchUiState

    data class Error(val message: String?) : SearchUiState
}

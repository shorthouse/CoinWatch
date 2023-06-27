package dev.shorthouse.cryptodata.ui.screen.list

import dev.shorthouse.cryptodata.model.CoinListItem

sealed interface ListUiState {
    object Loading : ListUiState
    data class Success(val data: List<CoinListItem>) : ListUiState
    object Error : ListUiState
}

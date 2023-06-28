package dev.shorthouse.cryptodata.ui.screen.list

import dev.shorthouse.cryptodata.model.Coin

sealed interface ListUiState {
    object Loading : ListUiState
    data class Success(val data: List<Coin>) : ListUiState
    object Error : ListUiState
}

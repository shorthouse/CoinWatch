package dev.shorthouse.cryptodata.ui.screen.detail

import dev.shorthouse.cryptodata.model.CoinDetail

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val data: CoinDetail?) : DetailUiState
    object Error : DetailUiState
}

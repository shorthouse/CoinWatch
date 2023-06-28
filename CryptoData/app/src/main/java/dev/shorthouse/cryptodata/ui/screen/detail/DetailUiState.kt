package dev.shorthouse.cryptodata.ui.screen.detail

import dev.shorthouse.cryptodata.model.Coin

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(
        val coinDetail: Coin?,
        val chartPeriodDays: String
    ) : DetailUiState
    object Error : DetailUiState
}

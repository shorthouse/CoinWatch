package dev.shorthouse.cryptodata.ui.screen.detail

import dev.shorthouse.cryptodata.model.CoinDetail

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(
        val coinDetail: CoinDetail?,
        val chartPeriodDays: String
    ) : DetailUiState
    object Error : DetailUiState
}

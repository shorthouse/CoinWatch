package dev.shorthouse.cryptodata.ui.screen.detail

import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import kotlin.time.Duration

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(
        val coinDetail: CoinDetail,
        val coinChart: CoinChart,
        val chartPeriod: Duration
    ) : DetailUiState
    data class Error(val message: String?) : DetailUiState
}

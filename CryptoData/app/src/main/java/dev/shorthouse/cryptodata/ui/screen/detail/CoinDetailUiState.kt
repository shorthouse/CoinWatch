package dev.shorthouse.cryptodata.ui.screen.detail

import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import kotlin.time.Duration

sealed interface CoinDetailUiState {
    object Loading : CoinDetailUiState
    data class Success(
        val coinDetail: CoinDetail,
        val coinChart: CoinChart,
        val chartPeriod: Duration
    ) : CoinDetailUiState
    data class Error(val message: String?) : CoinDetailUiState
}

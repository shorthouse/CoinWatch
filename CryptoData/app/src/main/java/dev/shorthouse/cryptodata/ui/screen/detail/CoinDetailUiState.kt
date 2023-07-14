package dev.shorthouse.cryptodata.ui.screen.detail

import dev.shorthouse.cryptodata.model.ChartPeriod
import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail

sealed interface CoinDetailUiState {
    object Loading : CoinDetailUiState
    data class Success(
        val coinDetail: CoinDetail,
        val coinChart: CoinChart,
        val chartPeriod: ChartPeriod
    ) : CoinDetailUiState
    data class Error(val message: String?) : CoinDetailUiState
}

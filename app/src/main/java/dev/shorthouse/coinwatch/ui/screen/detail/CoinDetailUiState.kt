package dev.shorthouse.coinwatch.ui.screen.detail

import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetail
import dev.shorthouse.coinwatch.ui.model.ChartPeriod

sealed interface CoinDetailUiState {
    data object Loading : CoinDetailUiState
    data class Success(
        val coinDetail: CoinDetail,
        val coinChart: CoinChart,
        val chartPeriod: ChartPeriod,
        val isCoinFavourite: Boolean
    ) : CoinDetailUiState
    data class Error(val message: String?) : CoinDetailUiState
}

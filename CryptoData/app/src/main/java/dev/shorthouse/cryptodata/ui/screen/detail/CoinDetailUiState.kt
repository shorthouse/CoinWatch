package dev.shorthouse.cryptodata.ui.screen.detail

import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.ui.model.ChartPeriod

sealed interface CoinDetailUiState {
    object Loading : CoinDetailUiState
    data class Success(
        val coinDetail: CoinDetail,
        val coinChart: CoinChart,
        val chartPeriod: ChartPeriod,
        val isCoinFavourite: Boolean,
    ) : CoinDetailUiState
    data class Error(val message: String?) : CoinDetailUiState
}

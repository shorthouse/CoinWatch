package dev.shorthouse.coinwatch.ui.screen.details

import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetail
import dev.shorthouse.coinwatch.ui.model.ChartPeriod

sealed interface DetailsUiState {
    object Loading : DetailsUiState
    data class Success(
        val coinDetail: CoinDetail,
        val coinChart: CoinChart,
        val chartPeriod: ChartPeriod,
        val isCoinFavourite: Boolean
    ) : DetailsUiState

    data class Error(val message: String?) : DetailsUiState
}

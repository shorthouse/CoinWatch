package dev.shorthouse.coinwatch.ui.screen.details

import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.ui.model.ChartPeriod

sealed interface DetailsUiState {
    object Loading : DetailsUiState
    data class Success(
        val coinDetails: CoinDetails,
        val coinChart: CoinChart,
        val chartPeriod: ChartPeriod,
        val isCoinFavourite: Boolean
    ) : DetailsUiState

    data class Error(val message: String?) : DetailsUiState
}

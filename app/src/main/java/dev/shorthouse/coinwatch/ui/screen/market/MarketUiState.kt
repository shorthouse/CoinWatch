package dev.shorthouse.coinwatch.ui.screen.market

import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.model.Coin
import kotlinx.collections.immutable.ImmutableList

sealed interface MarketUiState {
    data class Success(
        val coins: ImmutableList<Coin>,
        val coinSort: CoinSort,
        val showCoinSortBottomSheet: Boolean
    ) : MarketUiState

    data class Error(val message: String?) : MarketUiState
    object Loading : MarketUiState
}

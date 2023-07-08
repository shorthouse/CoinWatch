package dev.shorthouse.cryptodata.ui.screen.list

import dev.shorthouse.cryptodata.model.Coin

sealed interface CoinListUiState {
    object Loading : CoinListUiState
    data class Success(val coins: List<Coin>) : CoinListUiState
    data class Error(val message: String?) : CoinListUiState
}

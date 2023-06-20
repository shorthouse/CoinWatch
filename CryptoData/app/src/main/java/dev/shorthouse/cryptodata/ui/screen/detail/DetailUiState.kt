package dev.shorthouse.cryptodata.ui.screen.detail

import dev.shorthouse.cryptodata.model.CoinDetail

data class DetailUiState(
    val coinDetail: CoinDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

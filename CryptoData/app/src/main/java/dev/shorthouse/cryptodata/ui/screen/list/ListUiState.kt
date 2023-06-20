package dev.shorthouse.cryptodata.ui.screen.list

import dev.shorthouse.cryptodata.model.Coin

data class ListUiState(
    val cryptocurrencies: List<Coin> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

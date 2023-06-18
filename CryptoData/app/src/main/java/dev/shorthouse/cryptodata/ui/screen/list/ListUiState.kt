package dev.shorthouse.cryptodata.ui.screen.list

import dev.shorthouse.cryptodata.model.Cryptocurrency

data class ListUiState(
    val isLoading: Boolean = false,
    val cryptocurrencies: List<Cryptocurrency> = emptyList(),
    val error: String = "",
)

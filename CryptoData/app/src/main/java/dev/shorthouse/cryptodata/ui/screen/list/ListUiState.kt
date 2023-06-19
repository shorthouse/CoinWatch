package dev.shorthouse.cryptodata.ui.screen.list

import dev.shorthouse.cryptodata.model.Cryptocurrency

data class ListUiState(
    val cryptocurrencies: List<Cryptocurrency> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

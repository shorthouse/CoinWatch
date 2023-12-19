package dev.shorthouse.coinwatch

import dev.shorthouse.coinwatch.navigation.NavigationBarScreen

data class MainActivityUiState(
    val startDestination: NavigationBarScreen = NavigationBarScreen.Market,
    val isLoading: Boolean = false
)

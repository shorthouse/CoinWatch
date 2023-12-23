package dev.shorthouse.coinwatch

import dev.shorthouse.coinwatch.navigation.NavigationBarScreen

data class MainActivityUiState(
    val startScreen: NavigationBarScreen = NavigationBarScreen.Market,
    val isLoading: Boolean = false
)

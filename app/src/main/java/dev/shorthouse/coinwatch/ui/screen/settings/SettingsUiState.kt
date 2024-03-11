package dev.shorthouse.coinwatch.ui.screen.settings

import dev.shorthouse.coinwatch.data.preferences.global.StartScreen

data class SettingsUiState(
    val startScreen: StartScreen = StartScreen.Market,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

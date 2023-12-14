package dev.shorthouse.coinwatch.ui.screen.settings

import dev.shorthouse.coinwatch.data.userPreferences.StartDestination

data class SettingsUiState(
    val startDestination: StartDestination = StartDestination.Market,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

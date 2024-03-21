package dev.shorthouse.coinwatch.ui.screen.settings

import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.data.preferences.global.StartScreen

data class SettingsUiState(
    val currency: Currency = Currency.USD,
    val isCurrencySheetShown: Boolean = false,
    val startScreen: StartScreen = StartScreen.Market,
    val isStartScreenSheetShown: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

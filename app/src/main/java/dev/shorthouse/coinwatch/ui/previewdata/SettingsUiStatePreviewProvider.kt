package dev.shorthouse.coinwatch.ui.previewdata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.shorthouse.coinwatch.data.userPreferences.StartScreen
import dev.shorthouse.coinwatch.ui.screen.settings.SettingsUiState

class SettingsUiStatePreviewProvider : PreviewParameterProvider<SettingsUiState> {
    override val values = sequenceOf(
        SettingsUiState(
            startScreen = StartScreen.Favourites
        ),
        SettingsUiState(
            errorMessage = "No internet connection"
        ),
        SettingsUiState(
            isLoading = true
        )
    )
}

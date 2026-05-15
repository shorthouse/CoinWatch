package dev.shorthouse.coinwatch.ui.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.preview.SettingsUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun SettingsScreenScreenshotTest(
    @PreviewParameter(SettingsUiStatePreviewProvider::class) uiState: SettingsUiState,
) {
    AppTheme {
        SettingsScreen(
            uiState = uiState,
            onNavigateUp = {},
            onUpdateCurrency = {},
            onUpdateIsCurrencySheetShown = {},
            onUpdateStartScreen = {},
            onUpdateIsStartScreenSheetShown = {}
        )
    }
}

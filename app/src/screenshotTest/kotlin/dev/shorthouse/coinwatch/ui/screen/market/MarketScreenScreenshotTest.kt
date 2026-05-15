package dev.shorthouse.coinwatch.ui.screen.market

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.preview.MarketUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun MarketScreenScreenshotTest(
    @PreviewParameter(MarketUiStatePreviewProvider::class) uiState: MarketUiState,
) {
    AppTheme {
        MarketScreen(
            uiState = uiState,
            onCoinClick = {},
            onNavigateSettings = {},
            onUpdateCoinSort = {},
            onRefresh = {},
            onDismissError = {}
        )
    }
}

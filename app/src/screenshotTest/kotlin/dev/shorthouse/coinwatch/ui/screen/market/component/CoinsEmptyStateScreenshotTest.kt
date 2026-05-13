package dev.shorthouse.coinwatch.ui.screen.market.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview(heightDp = 400)
@Composable
fun CoinsEmptyStateScreenshotTest() {
    AppTheme {
        CoinsEmptyState()
    }
}

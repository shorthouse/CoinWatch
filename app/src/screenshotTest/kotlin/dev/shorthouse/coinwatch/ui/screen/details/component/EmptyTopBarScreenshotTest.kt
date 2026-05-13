package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun EmptyTopBarScreenshotTest() {
    AppTheme {
        EmptyTopBar(onNavigateUp = {})
    }
}

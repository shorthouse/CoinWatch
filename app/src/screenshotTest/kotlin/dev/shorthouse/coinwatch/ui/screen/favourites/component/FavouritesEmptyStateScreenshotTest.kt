package dev.shorthouse.coinwatch.ui.screen.favourites.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview(heightDp = 450)
@Composable
fun FavouritesEmptyStateScreenshotTest() {
    AppTheme {
        FavouritesEmptyState()
    }
}

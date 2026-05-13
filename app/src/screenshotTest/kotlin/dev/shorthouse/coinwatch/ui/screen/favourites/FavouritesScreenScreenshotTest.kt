package dev.shorthouse.coinwatch.ui.screen.favourites

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.previewdata.FavouritesUiStatePreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun FavouritesScreenScreenshotTest(
    @PreviewParameter(FavouritesUiStatePreviewProvider::class) uiState: FavouritesUiState,
) {
    AppTheme {
        FavouriteScreen(
            uiState = uiState,
            onCoinClick = {},
            onUpdateIsFavouritesCondensed = {},
            onUpdateCoinSort = {},
            onRefresh = {},
            onDismissError = {},
        )
    }
}

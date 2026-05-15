package dev.shorthouse.coinwatch.ui.screen.details

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.preview.DetailsScreenPreviewState
import dev.shorthouse.coinwatch.ui.preview.DetailsScreenPreviewStateProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun DetailsScreenScreenshotTest(
    @PreviewParameter(DetailsScreenPreviewStateProvider::class)
    previewState: DetailsScreenPreviewState,
) {
    AppTheme {
        DetailsScreen(
            uiState = previewState.uiState,
            onNavigateUp = {},
            onClickFavouriteCoin = {},
            onClickChartPeriod = {},
            scrollState = rememberScrollState(initial = previewState.scrollPosition)
        )
    }
}

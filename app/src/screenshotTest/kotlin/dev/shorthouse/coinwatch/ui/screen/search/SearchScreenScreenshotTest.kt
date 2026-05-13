package dev.shorthouse.coinwatch.ui.screen.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.previewdata.SearchScreenPreviewState
import dev.shorthouse.coinwatch.ui.previewdata.SearchScreenPreviewStateProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun SearchScreenScreenshotTest(
    @PreviewParameter(SearchScreenPreviewStateProvider::class) previewState: SearchScreenPreviewState,
) {
    AppTheme {
        SearchScreen(
            uiState = previewState.uiState,
            searchQuery = previewState.searchQuery,
            onSearchQueryChange = {},
            onCoinClick = {}
        )
    }
}

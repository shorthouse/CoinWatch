package dev.shorthouse.coinwatch.ui.screen.search.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.model.SearchCoin
import dev.shorthouse.coinwatch.ui.previewdata.SearchCoinPreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun SearchListItemScreenshotTest(
    @PreviewParameter(SearchCoinPreviewProvider::class) searchCoin: SearchCoin,
) {
    AppTheme {
        SearchListItem(
            searchCoin = searchCoin,
            onCoinClick = {},
            cardShape = MaterialTheme.shapes.medium
        )
    }
}

package dev.shorthouse.coinwatch.ui.screen.favourites.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoin
import dev.shorthouse.coinwatch.ui.previewdata.FavouriteCoinPreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun FavouriteCondensedItemScreenshotTest(
    @PreviewParameter(FavouriteCoinPreviewProvider::class) favouriteCoin: FavouriteCoin,
) {
    AppTheme {
        FavouriteCondensedItem(
            favouriteCoin = favouriteCoin,
            onCoinClick = {},
            cardShape = MaterialTheme.shapes.medium,
        )
    }
}

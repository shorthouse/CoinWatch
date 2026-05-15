package dev.shorthouse.coinwatch.ui.screen.market.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.data.source.local.database.model.Coin
import dev.shorthouse.coinwatch.ui.preview.CoinPreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun MarketCoinItemScreenshotTest(
    @PreviewParameter(CoinPreviewProvider::class) coin: Coin,
) {
    AppTheme {
        MarketCoinItem(
            coin = coin,
            onCoinClick = {},
            cardShape = MaterialTheme.shapes.medium
        )
    }
}

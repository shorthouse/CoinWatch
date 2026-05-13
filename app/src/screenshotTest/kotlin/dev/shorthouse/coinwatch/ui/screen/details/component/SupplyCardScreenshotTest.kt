package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun SupplyCardScreenshotTest() {
    AppTheme {
        SupplyCard(
            circulatingSupply = "120,186,525",
            totalSupply = "120,500,000",
            maxSupply = "210,000,000"
        )
    }
}

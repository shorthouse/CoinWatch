package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf

@PreviewTest
@Preview
@Composable
fun AboutCardScreenshotTest() {
    AppTheme {
        AboutCard(
            description = "Ethereum is a decentralized blockchain with smart contract functionality.",
            tags = persistentListOf("smart-contracts", "staking", "layer-1"),
            listedDate = "7 Aug 2015"
        )
    }
}

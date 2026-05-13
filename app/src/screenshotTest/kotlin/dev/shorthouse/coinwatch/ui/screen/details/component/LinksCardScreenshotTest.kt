package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf

@PreviewTest
@Preview
@Composable
fun LinksCardScreenshotTest() {
    AppTheme {
        LinksCard(
            links = persistentListOf(
                CoinLink(
                    type = CoinLinkType.Website,
                    url = "https://ethereum.org",
                ),
                CoinLink(
                    type = CoinLinkType.GitHub,
                    url = "https://github.com/ethereum",
                ),
                CoinLink(
                    type = CoinLinkType.Reddit,
                    url = "https://reddit.com/r/ethereum",
                )
            ),
            onClickLink = {}
        )
    }
}

package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Launch
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@Composable
fun LinksCard(
    links: ImmutableList<CoinLink>,
    onClickLink: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Column {
            links.forEachIndexed { linkIndex, link ->
                val linkLabel = stringResource(link.type.getNameStringId())

                if (linkIndex != 0) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.primaryContainer)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.extraSmall)
                        .clickable { onClickLink(link.url) }
                        .padding(vertical = 16.dp, horizontal = 12.dp)
                ) {
                    Text(
                        text = linkLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Launch,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(R.string.cd_open_link, linkLabel)
                    )
                }
            }
        }
    }
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun LinksCardPreview() {
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
                url = "https://www.reddit.com/r/ethereum/",
            ),
            CoinLink(
                type = CoinLinkType.X,
                url = "https://twitter.com/ethereum",
            ),
            CoinLink(
                type = CoinLinkType.YouTube,
                url = "https://www.youtube.com/@EthereumProtocol",
            )
        ),
        onClickLink = {}
    )

}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun LinksCardLongLabelPreview() {
    LinksCard(
        links = persistentListOf(
            CoinLink(
                type = CoinLinkType.Whitepaper,
                url = "https://ethereum.org/developers",
            )
        ),
        onClickLink = {}
    )

}

@StringRes
private fun CoinLinkType.getNameStringId(): Int {
    return when (this) {
        CoinLinkType.Website -> R.string.coin_link_website
        CoinLinkType.Whitepaper -> R.string.coin_link_whitepaper
        CoinLinkType.Discord -> R.string.coin_link_discord
        CoinLinkType.GitHub -> R.string.coin_link_github
        CoinLinkType.Reddit -> R.string.coin_link_reddit
        CoinLinkType.Telegram -> R.string.coin_link_telegram
        CoinLinkType.X -> R.string.coin_link_x
        CoinLinkType.YouTube -> R.string.coin_link_youtube
    }
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun LinksCardEmptyPreview() {
    LinksCard(
        links = persistentListOf(),
        onClickLink = {}
    )

}

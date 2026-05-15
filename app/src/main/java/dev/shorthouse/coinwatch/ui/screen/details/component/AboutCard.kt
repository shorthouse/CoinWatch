package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutCard(
    description: String,
    tags: ImmutableList<String>,
    listedDate: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.animateContentSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            if (description.isNotBlank()) {
                DescriptionSection(description = description)
            }

            if (tags.isNotEmpty()) {
                TagsSection(tags = tags)
            }

            ListedDateSection(listedDate = listedDate)
        }
    }
}

@Composable
private fun DescriptionSection(
    description: String,
    modifier: Modifier = Modifier,
) {
    var isDescriptionExpanded by remember(description) { mutableStateOf(false) }
    var isDescriptionExpandable by remember(description) { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = if (isDescriptionExpanded) {
                Int.MAX_VALUE
            } else {
                4
            },
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                if (!isDescriptionExpanded) {
                    isDescriptionExpandable = textLayoutResult.hasVisualOverflow
                }
            },
            modifier = Modifier.weight(1f)
        )

        if (isDescriptionExpandable) {
            IconButton(
                onClick = { isDescriptionExpanded = !isDescriptionExpanded },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = if (isDescriptionExpanded) {
                        Icons.Rounded.KeyboardArrowUp
                    } else {
                        Icons.Rounded.KeyboardArrowDown
                    },
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = if (isDescriptionExpanded) {
                        stringResource(R.string.cd_collapse_about_description)
                    } else {
                        stringResource(R.string.cd_expand_about_description)
                    }

                )
            }
        }
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.primaryContainer)
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun TagsSection(
    tags: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.list_item_tags),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                TagChip(text = tag)
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.primaryContainer)
    }
}

@Composable
private fun TagChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ListedDateSection(
    listedDate: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.list_item_listed_date),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = listedDate,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun AboutCardPreview() {
    AboutCard(
        description = "Ethereum is a decentralized blockchain with smart contract functionality.",
        tags = persistentListOf("smart-contracts", "staking", "layer-1"),
        listedDate = "7 Aug 2015"
    )

}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun AboutCardLongDescriptionPreview() {
    AboutCard(
        description = "Bitcoin is the first decentralized cryptocurrency. It allows value to be " +
            "transferred without a central bank or single administrator, using a public " +
            "ledger that is maintained by a distributed network. Its fixed supply and " +
            "global liquidity have made it a benchmark asset across crypto markets, " +
            "while its open-source protocol continues to be developed by contributors " +
            "around the world.",
        tags = persistentListOf("store-of-value", "payments", "proof-of-work"),
        listedDate = "1 Jan 2017"
    )

}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun AboutCardNoTagsPreview() {
    AboutCard(
        description = "Ethereum is a decentralized blockchain with smart contract functionality.",
        tags = persistentListOf(),
        listedDate = "7 Aug 2015"
    )

}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun AboutCardNoDescriptionPreview() {
    AboutCard(
        description = "",
        tags = persistentListOf("defi", "staking", "layer-1", "smart-contracts"),
        listedDate = "7 Aug 2015"
    )

}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun AboutCardWrappingTagsPreview() {
    AboutCard(
        description = "Ethereum is a decentralized blockchain with smart contract functionality.",
        tags = persistentListOf(
            "smart-contracts",
            "decentralized-finance",
            "proof-of-stake",
            "layer-1",
            "governance",
            "scaling",
            "ecosystem"
        ),
        listedDate = "7 Aug 2015"
    )

}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun AboutCardListedDateOnlyPreview() {
    AboutCard(
        description = "",
        tags = persistentListOf(),
        listedDate = "7 Aug 2015"
    )

}

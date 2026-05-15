package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.common.Constants.MISSING_VALUE_PLACEHOLDER
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@Composable
fun SupplyCard(
    circulatingSupply: String,
    totalSupply: String,
    maxSupply: String,
    modifier: Modifier = Modifier,
) {
    val supplyItems = remember(circulatingSupply, totalSupply, maxSupply) {
        listOf(
            SupplyListItem(
                nameId = R.string.list_item_circulating_supply,
                value = circulatingSupply
            ),
            SupplyListItem(
                nameId = R.string.list_item_total_supply,
                value = totalSupply
            ),
            SupplyListItem(
                nameId = R.string.list_item_max_supply,
                value = maxSupply
            )
        )
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                supplyItems.forEachIndexed { supplyIndex, supplyListItem ->
                    if (supplyIndex != 0) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.primaryContainer)
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(supplyListItem.nameId),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                        )

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = supplyListItem.value,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

private data class SupplyListItem(
    @StringRes val nameId: Int,
    val value: String,
)

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun SupplyCardPreview() {
    SupplyCard(
        circulatingSupply = "120,186,525",
        totalSupply = "120,500,000",
        maxSupply = "210,000,000"
    )

}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun SupplyCardEmptyPreview() {
    SupplyCard(
        circulatingSupply = MISSING_VALUE_PLACEHOLDER,
        totalSupply = MISSING_VALUE_PLACEHOLDER,
        maxSupply = MISSING_VALUE_PLACEHOLDER
    )

}

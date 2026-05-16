package dev.shorthouse.coinwatch.ui.screen.market.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@Composable
fun MarketDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onNavigateSettings: () -> Unit,
    modifier: Modifier = Modifier,
    usePlatformPopup: Boolean = true,
) {
    if (usePlatformPopup) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = modifier
        ) {
            MarketDropdownMenuItems(onNavigateSettings = onNavigateSettings)
        }
    } else if (expanded) {
        Surface(
            shape = MaterialTheme.shapes.extraSmall,
            color = MaterialTheme.colorScheme.surface,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(IntrinsicSize.Max)
            ) {
                MarketDropdownMenuItems(onNavigateSettings = onNavigateSettings)
            }
        }
    }
}

@Composable
private fun MarketDropdownMenuItems(
    onNavigateSettings: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Text(
                text = stringResource(R.string.dropdown_option_settings),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        onClick = onNavigateSettings
    )
}

@Composable
@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
private fun MarketDropdownMenuPreview() {
    MarketDropdownMenu(
        expanded = true,
        onDismissRequest = {},
        onNavigateSettings = {},
        usePlatformPopup = false
    )

}

package dev.shorthouse.coinwatch.ui.screen.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun SettingsGroup(
    title: String,
    modifier: Modifier = Modifier,
    showBottomDivider: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
        )

        content()

        if (showBottomDivider) {
            HorizontalDivider(color = MaterialTheme.colorScheme.primaryContainer)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SettingsGroupPreview() {
    AppTheme {
        SettingsGroup(title = "Preferences") {
            SettingsItem(
                title = "Start screen",
                subtitle = "Market",
                leadingIcon = Icons.Rounded.Home,
                trailingIcon = Icons.Rounded.ChevronRight,
                onClick = {}
            )

            SettingsItem(
                title = "CoinWatch version",
                subtitle = "1.4.1",
                leadingIcon = Icons.Rounded.Smartphone,
                onClick = {}
            )
        }
    }
}

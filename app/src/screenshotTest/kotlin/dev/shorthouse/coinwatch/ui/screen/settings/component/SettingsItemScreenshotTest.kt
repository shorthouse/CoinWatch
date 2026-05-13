package dev.shorthouse.coinwatch.ui.screen.settings.component

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview(widthDp = 360, heightDp = 96)
@Composable
fun SettingsItemScreenshotTest() {
    AppTheme {
        SettingsItem(
            title = "Start screen",
            subtitle = "Market",
            leadingIcon = Icons.Rounded.Home,
            trailingIcon = Icons.Rounded.ChevronRight,
            onClick = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )
    }
}

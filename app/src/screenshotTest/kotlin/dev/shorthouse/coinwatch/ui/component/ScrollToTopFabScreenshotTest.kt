package dev.shorthouse.coinwatch.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun ScrollToTopFabScreenshotTest() {
    AppTheme {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(4.dp)
        ) {
            ScrollToTopFab(onClick = {})
        }
    }
}

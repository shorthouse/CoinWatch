package dev.shorthouse.coinwatch.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview(heightDp = 400)
@Composable
fun EmptyStateScreenshotTest() {
    AppTheme {
        EmptyState(
            image = painterResource(R.drawable.empty_state_coins),
            title = "No coins",
            subtitle = {
                Text(
                    text = "Check your internet connection",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )
    }
}

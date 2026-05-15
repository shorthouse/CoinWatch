package dev.shorthouse.coinwatch.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.shorthouse.coinwatch.R
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@Composable
fun ErrorState(
    message: String?,
    modifier: Modifier = Modifier
) {
    EmptyState(
        image = painterResource(R.drawable.error_state),
        title = stringResource(R.string.error_occurred),
        subtitle = {
            message?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        modifier = modifier
    )
}

@Composable
@Preview(heightDp = 400)
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
private fun ErrorStatePreview() {
    ErrorState(
        message = "No internet connection",
    )

}

package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@Composable
fun PulseSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(8.dp))

        content()
    }
}

@Composable
@Preview(showBackground = true)
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
private fun PulseSectionPreview() {
    PulseSection(
        title = "Market Mood",
        modifier = Modifier.padding(12.dp)
    ) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Text(
                text = "Section content",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

package dev.shorthouse.coinwatch.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun EmptyState(
    image: Painter,
    title: String,
    subtitle: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.size(250.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(4.dp))

            subtitle()
        }
    }
}

@Composable
@Preview
private fun EmptyStatePreview() {
    AppTheme {
        EmptyState(
            image = painterResource(R.drawable.empty_state_coins),
            title = "No coins",
            subtitle = {
                Text(
                    text = stringResource(R.string.empty_state_coins_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}

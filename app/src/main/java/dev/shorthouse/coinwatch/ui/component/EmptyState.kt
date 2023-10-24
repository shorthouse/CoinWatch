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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R

@Composable
fun EmptyState(
    image: Painter,
    title: String,
    subtitle: @Composable () -> Unit,
    modifier: Modifier = Modifier
    // onRetry: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
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

            Spacer(Modifier.height(24.dp))

//            Button(
//                onClick = onRetry,
//                shape = MaterialTheme.shapes.small,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.surface,
//                    contentColor = MaterialTheme.colorScheme.onSurface
//                )
//            ) {
//                Text(
//                    text = stringResource(R.string.button_retry),
//                    style = MaterialTheme.typography.titleSmall
//                )
//            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EmptyStatePreview() {
    EmptyState(
        image = painterResource(R.drawable.empty_state_coins),
        title = "No coins",
        subtitle = {
            Text(text = "Please try again later")
        }
    )
}

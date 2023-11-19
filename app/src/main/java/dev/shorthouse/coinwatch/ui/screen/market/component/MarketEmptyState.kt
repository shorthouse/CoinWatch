package dev.shorthouse.coinwatch.ui.screen.market.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.ui.component.EmptyState
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@Composable
fun MarketEmptyState(modifier: Modifier = Modifier) {
    EmptyState(
        image = painterResource(R.drawable.empty_state_coins),
        title = stringResource(R.string.empty_state_coins_title),
        subtitle = {
            Text(
                text = stringResource(R.string.empty_state_coins_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier
    )
}

@Composable
@Preview
private fun MarketEmptyStatePreview() {
    AppTheme {
        MarketEmptyState()
    }
}

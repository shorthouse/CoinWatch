package dev.shorthouse.coinwatch.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.MoreTime
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import dev.shorthouse.coinwatch.ui.theme.AppTheme


@Composable
fun CoinSortChip(
    coinSort: CoinSort,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        label = { Text(text = stringResource(coinSort.nameId)) },
        onClick = onClick,
        selected = selected,
        leadingIcon = {
            val imageVector = when (coinSort) {
                CoinSort.MarketCap -> Icons.Rounded.BarChart
                CoinSort.Popular -> Icons.Rounded.Whatshot
                CoinSort.Gainers -> Icons.AutoMirrored.Rounded.TrendingUp
                CoinSort.Losers -> Icons.AutoMirrored.Rounded.TrendingDown
                CoinSort.Newest -> Icons.Rounded.MoreTime
            }

            Icon(
                imageVector = imageVector,
                tint = if (selected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                contentDescription = null
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedContainerColor = MaterialTheme.colorScheme.surface,
            selectedLabelColor = MaterialTheme.colorScheme.onSurface,
        ),
        shape = MaterialTheme.shapes.small,
        border = null,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun CoinSortChipUnselectedPreview() {
    AppTheme {
        CoinSortChip(
            coinSort = CoinSort.MarketCap,
            selected = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CoinSortChipSelectedPreview() {
    AppTheme {
        CoinSortChip(
            coinSort = CoinSort.Gainers,
            selected = true,
            onClick = {}
        )
    }
}

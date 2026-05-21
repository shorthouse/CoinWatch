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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.source.local.datastore.common.CoinSort
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@Composable
fun CoinSortChip(
    coinSort: CoinSort,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        label = { Text(text = stringResource(coinSort.labelId)) },
        onClick = onClick,
        selected = selected,
        leadingIcon = {
            Icon(
                imageVector = coinSort.imageVector,
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

private val CoinSort.labelId: Int
    get() = when (this) {
        CoinSort.MarketCap -> R.string.market_coin_sort_market_cap
        CoinSort.Popular -> R.string.market_coin_sort_popular
        CoinSort.Gainers -> R.string.market_coin_sort_gainers
        CoinSort.Losers -> R.string.market_coin_sort_losers
        CoinSort.Newest -> R.string.market_coin_sort_newest
    }

private val CoinSort.imageVector: ImageVector
    get() = when (this) {
        CoinSort.MarketCap -> Icons.Rounded.BarChart
        CoinSort.Popular -> Icons.Rounded.Whatshot
        CoinSort.Gainers -> Icons.AutoMirrored.Rounded.TrendingUp
        CoinSort.Losers -> Icons.AutoMirrored.Rounded.TrendingDown
        CoinSort.Newest -> Icons.Rounded.MoreTime
    }

@Preview(showBackground = true)
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun CoinSortChipUnselectedPreview() {
    CoinSortChip(
        coinSort = CoinSort.MarketCap,
        selected = false,
        onClick = {}
    )

}

@Preview(showBackground = true)
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun CoinSortChipSelectedPreview() {
    CoinSortChip(
        coinSort = CoinSort.Gainers,
        selected = true,
        onClick = {}
    )

}

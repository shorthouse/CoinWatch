package dev.shorthouse.coinwatch.ui.screen.market.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.InsertChartOutlined
import androidx.compose.material.icons.rounded.Percent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinSortBottomSheet(
    sheetState: SheetState,
    selectedCoinSort: CoinSort,
    onDismissRequest: () -> Unit,
    onCoinSortSelected: (CoinSort) -> Unit,
    modifier: Modifier = Modifier
) {
    val coinSortOptions = remember {
        persistentListOf(
            CoinSortOption(
                icon = Icons.Rounded.InsertChartOutlined,
                labelId = R.string.coin_sort_market_cap,
                coinSort = CoinSort.MarketCap
            ),
            CoinSortOption(
                icon = Icons.Rounded.CurrencyBitcoin,
                labelId = R.string.coin_sort_price,
                coinSort = CoinSort.Price
            ),
            CoinSortOption(
                icon = Icons.Rounded.Percent,
                labelId = R.string.coin_sort_price_change_24h,
                coinSort = CoinSort.PriceChange24h
            ),
            CoinSortOption(
                icon = Icons.Rounded.CurrencyExchange,
                labelId = R.string.coin_sort_volume_24h,
                coinSort = CoinSort.Volume24h
            )
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        dragHandle = null,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.coin_sort_bottom_sheet_title),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))

            coinSortOptions.forEach { option ->
                CoinSortOption(
                    icon = option.icon,
                    label = stringResource(option.labelId),
                    isSelected = option.coinSort == selectedCoinSort,
                    onSelected = { onCoinSortSelected(option.coinSort) }
                )
            }
        }
    }
}

@Composable
private fun CoinSortOption(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val optionBackgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(optionBackgroundColor)
            .clickable(onClick = onSelected)
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private data class CoinSortOption(
    val icon: ImageVector,
    @StringRes val labelId: Int,
    val coinSort: CoinSort
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CoinSortBottomSheetPreview() {
    AppTheme {
        var selectedCoinSort by remember { mutableStateOf(CoinSort.MarketCap) }

        CoinSortBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            selectedCoinSort = selectedCoinSort,
            onDismissRequest = {},
            onCoinSortSelected = { selectedCoinSort = it }
        )
    }
}

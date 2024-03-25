package dev.shorthouse.coinwatch.ui.screen.favourites.component

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.InsertChartOutlined
import androidx.compose.material.icons.rounded.Percent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.preferences.global.CoinSort
import dev.shorthouse.coinwatch.ui.component.AppBottomSheet
import dev.shorthouse.coinwatch.ui.component.BottomSheetOption
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinSortBottomSheet(
    sheetState: SheetState,
    selectedCoinSort: CoinSort,
    onCoinSortSelected: (CoinSort) -> Unit,
    onDismissRequest: () -> Unit,
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
                labelId = R.string.coin_sort_price_change,
                coinSort = CoinSort.PriceChange24h
            ),
            CoinSortOption(
                icon = Icons.Rounded.CurrencyExchange,
                labelId = R.string.coin_sort_volume,
                coinSort = CoinSort.Volume24h
            )
        )
    }

    AppBottomSheet(
        title = stringResource(R.string.coin_sort_bottom_sheet_title),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
    ) {
        coinSortOptions.forEach { option ->
            BottomSheetOption(
                icon = option.icon,
                label = stringResource(option.labelId),
                isSelected = option.coinSort == selectedCoinSort,
                onSelected = { onCoinSortSelected(option.coinSort) }
            )
        }
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
private fun CoinSortBottomSheetPreview() {
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

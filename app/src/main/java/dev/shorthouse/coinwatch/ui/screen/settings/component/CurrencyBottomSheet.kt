package dev.shorthouse.coinwatch.ui.screen.settings.component

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.CurrencyPound
import androidx.compose.material.icons.rounded.Euro
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
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.ui.component.AppBottomSheet
import dev.shorthouse.coinwatch.ui.component.BottomSheetOption
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyBottomSheet(
    sheetState: SheetState,
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyOptions = remember {
        persistentListOf(
            CurrencyOption(
                icon = Icons.Rounded.AttachMoney,
                labelId = R.string.currency_usd,
                currency = Currency.USD
            ),
            CurrencyOption(
                icon = Icons.Rounded.CurrencyPound,
                labelId = R.string.currency_gbp,
                currency = Currency.GBP
            ),
            CurrencyOption(
                icon = Icons.Rounded.Euro,
                labelId = R.string.currency_eur,
                currency = Currency.EUR
            )
        )
    }

    AppBottomSheet(
        title = stringResource(R.string.currency_bottom_sheet_title),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
    ) {
        currencyOptions.forEach { option ->
            BottomSheetOption(
                icon = option.icon,
                label = stringResource(option.labelId),
                isSelected = option.currency == selectedCurrency,
                onSelected = { onCurrencySelected(option.currency) }
            )
        }
    }
}

private data class CurrencyOption(
    val icon: ImageVector,
    @StringRes val labelId: Int,
    val currency: Currency
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CurrencyBottomSheetPreview() {
    AppTheme {
        var selectedCurrency by remember { mutableStateOf(Currency.USD) }

        CurrencyBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            selectedCurrency = selectedCurrency,
            onDismissRequest = {},
            onCurrencySelected = { selectedCurrency = it }
        )
    }
}

package dev.shorthouse.coinwatch.ui.screen.settings.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@PreviewTest
@Preview(widthDp = 360, heightDp = 280)
@Composable
fun CurrencyBottomSheetScreenshotTest() {
    val density = LocalDensity.current
    val sheetState = remember {
        SheetState(
            skipPartiallyExpanded = true,
            positionalThreshold = { with(density) { 56.dp.toPx() } },
            velocityThreshold = { with(density) { 125.dp.toPx() } },
            initialValue = SheetValue.Expanded,
        )
    }

    AppTheme {
        CurrencyBottomSheet(
            sheetState = sheetState,
            selectedCurrency = Currency.USD,
            onCurrencySelected = {},
            onDismissRequest = {},
        )
    }
}

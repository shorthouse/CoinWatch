package dev.shorthouse.coinwatch.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@PreviewTest
@Preview(widthDp = 360, heightDp = 200)
@Composable
fun AppBottomSheetScreenshotTest() {
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
        AppBottomSheet(
            title = "Coin currency",
            onDismissRequest = {},
            sheetState = sheetState,
        ) {
            BottomSheetOption(
                icon = Icons.Rounded.CurrencyBitcoin,
                label = "Bitcoin",
                isSelected = true,
                onSelected = {},
            )
            BottomSheetOption(
                icon = Icons.Rounded.AttachMoney,
                label = "USD",
                isSelected = false,
                onSelected = {},
            )
        }
    }
}

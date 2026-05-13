package dev.shorthouse.coinwatch.ui.screen.settings.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.data.source.local.preferences.global.StartScreen
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@PreviewTest
@Preview(widthDp = 360, heightDp = 280)
@Composable
fun StartScreenBottomSheetScreenshotTest() {
    val density = LocalDensity.current
    val sheetState = remember {
        SheetState(
            skipPartiallyExpanded = true,
            density = density,
            initialValue = SheetValue.Expanded,
        )
    }

    AppTheme {
        StartScreenBottomSheet(
            sheetState = sheetState,
            selectedStartScreen = StartScreen.Favourites,
            onStartScreenSelected = {},
            onDismissRequest = {},
        )
    }
}

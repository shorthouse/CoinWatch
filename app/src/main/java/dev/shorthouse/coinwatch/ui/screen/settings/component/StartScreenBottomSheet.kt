package dev.shorthouse.coinwatch.ui.screen.settings.component

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.data.source.local.datastore.global.StartScreen
import dev.shorthouse.coinwatch.ui.component.AppBottomSheet
import dev.shorthouse.coinwatch.ui.component.BottomSheetOption
import kotlinx.collections.immutable.persistentListOf
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreenBottomSheet(
    sheetState: SheetState,
    selectedStartScreen: StartScreen,
    onStartScreenSelected: (StartScreen) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val startScreenOptions = remember {
        persistentListOf(
            StartScreenOption(
                icon = Icons.Rounded.BarChart,
                labelId = StartScreen.Market.nameId,
                startScreen = StartScreen.Market
            ),
            StartScreenOption(
                icon = Icons.Rounded.Favorite,
                labelId = StartScreen.Favourites.nameId,
                startScreen = StartScreen.Favourites
            ),
            StartScreenOption(
                icon = Icons.Rounded.Insights,
                labelId = StartScreen.Pulse.nameId,
                startScreen = StartScreen.Pulse
            ),
            StartScreenOption(
                icon = Icons.Rounded.Search,
                labelId = StartScreen.Search.nameId,
                startScreen = StartScreen.Search
            )
        )
    }

    AppBottomSheet(
        title = stringResource(R.string.start_screen_bottom_sheet_title),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
    ) {
        startScreenOptions.forEach { option ->
            BottomSheetOption(
                icon = option.icon,
                label = stringResource(option.labelId),
                isSelected = option.startScreen == selectedStartScreen,
                onSelected = { onStartScreenSelected(option.startScreen) }
            )
        }
    }
}

private data class StartScreenOption(
    val icon: ImageVector,
    @StringRes val labelId: Int,
    val startScreen: StartScreen,
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(widthDp = 360, heightDp = 280)
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun StartScreenBottomSheetPreview() {
    val density = LocalDensity.current
    val sheetState = remember {
        SheetState(
            skipPartiallyExpanded = true,
            positionalThreshold = { with(density) { 56.dp.toPx() } },
            velocityThreshold = { with(density) { 125.dp.toPx() } },
            initialValue = SheetValue.Expanded,
        )
    }

    var selectedStartScreen by remember { mutableStateOf(StartScreen.Favourites) }

    StartScreenBottomSheet(
        sheetState = sheetState,
        selectedStartScreen = selectedStartScreen,
        onStartScreenSelected = { selectedStartScreen = it },
        onDismissRequest = {},
    )

}

package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun ChartPeriodSelectorScreenshotTest() {
    AppTheme {
        ChartPeriodSelector(
            selectedChartPeriod = ChartPeriod.Week,
            onChartPeriodSelected = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

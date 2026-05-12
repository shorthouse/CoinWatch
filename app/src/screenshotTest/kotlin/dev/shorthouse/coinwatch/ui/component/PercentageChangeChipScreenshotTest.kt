package dev.shorthouse.coinwatch.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.ui.previewdata.PercentagePreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme

@PreviewTest
@Preview
@Composable
fun PercentageChangeChipScreenshotTest(
    @PreviewParameter(PercentagePreviewProvider::class) percentage: Percentage,
) {
    AppTheme {
        PercentageChangeChip(
            percentage = percentage
        )
    }
}

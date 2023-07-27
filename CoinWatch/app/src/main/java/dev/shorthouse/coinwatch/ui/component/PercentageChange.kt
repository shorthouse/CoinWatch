package dev.shorthouse.coinwatch.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.ui.previewdata.PercentagePreviewProvider
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import dev.shorthouse.coinwatch.ui.theme.NegativeRed
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen

@Composable
fun PercentageChange(
    percentage: Percentage,
    modifier: Modifier = Modifier
) {
    val textColor = when {
        percentage.isPositive -> PositiveGreen
        percentage.isNegative -> NegativeRed
        else -> MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = percentage.formattedAmount,
        style = MaterialTheme.typography.bodySmall,
        color = textColor,
        modifier = modifier
    )
}

@Composable
@Preview
private fun PercentageChangePreview(
    @PreviewParameter(PercentagePreviewProvider::class) percentage: Percentage
) {
    AppTheme {
        PercentageChange(
            percentage = percentage
        )
    }
}

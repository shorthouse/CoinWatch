package dev.shorthouse.cryptodata.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.ui.previewdata.PercentagePreviewProvider
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import dev.shorthouse.cryptodata.ui.theme.NegativeRed
import dev.shorthouse.cryptodata.ui.theme.PositiveGreen

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
        color = textColor,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier
    )
}

@Composable
@Preview(showBackground = true)
private fun PercentageChangePreview(
    @PreviewParameter(PercentagePreviewProvider::class) percentage: Percentage
) {
    AppTheme {
        PercentageChange(
            percentage = percentage
        )
    }
}

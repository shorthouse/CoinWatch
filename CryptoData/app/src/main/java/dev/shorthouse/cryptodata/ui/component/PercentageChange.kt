package dev.shorthouse.cryptodata.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.ui.previewdata.PercentagePreviewProvider
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import dev.shorthouse.cryptodata.ui.theme.NegativeRed
import dev.shorthouse.cryptodata.ui.theme.PositiveGreen
import dev.shorthouse.cryptodata.ui.theme.ZeroWhite

@Composable
fun PercentageChange(
    percentage: Percentage,
    modifier: Modifier = Modifier
) {
    val textColor = when {
        percentage.isPositive -> PositiveGreen
        percentage.isNegative -> NegativeRed
        else -> ZeroWhite
    }

    Text(
        text = percentage.formattedAmount,
        color = textColor,
        style = MaterialTheme.typography.bodyLarge,
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

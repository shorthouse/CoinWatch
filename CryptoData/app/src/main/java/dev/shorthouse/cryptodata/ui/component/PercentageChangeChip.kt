package dev.shorthouse.cryptodata.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.ui.previewdata.PercentagePreviewProvider
import dev.shorthouse.cryptodata.ui.theme.AppTheme
import dev.shorthouse.cryptodata.ui.theme.NegativeRed
import dev.shorthouse.cryptodata.ui.theme.PositiveGreen

@Composable
fun PercentageChangeChip(
    percentage: Percentage,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        percentage.isPositive -> PositiveGreen
        percentage.isNegative -> NegativeRed
        else -> Color.Black
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = backgroundColor
    ) {
        Text(
            text = percentage.formattedAmount,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(horizontal = 7.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PercentageChangeChipPreview(
    @PreviewParameter(PercentagePreviewProvider::class) percentage: Percentage
) {
    AppTheme {
        PercentageChangeChip(
            percentage = percentage
        )
    }
}
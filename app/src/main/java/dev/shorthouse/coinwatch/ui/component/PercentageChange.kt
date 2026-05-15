package dev.shorthouse.coinwatch.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.ui.preview.PercentagePreviewProvider
import dev.shorthouse.coinwatch.ui.theme.NegativeRed
import dev.shorthouse.coinwatch.ui.theme.PositiveGreen
import androidx.compose.ui.tooling.preview.PreviewWrapper
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper

@Composable
fun PercentageChange(
    percentage: Percentage,
    modifier: Modifier = Modifier,
) {
    val textColor = when {
        percentage.isPositive -> PositiveGreen
        percentage.isNegative -> NegativeRed
        else -> MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = percentage.formattedAmount,
        style = MaterialTheme.typography.bodyMedium,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
private fun PercentageChangePreview(
    @PreviewParameter(PercentagePreviewProvider::class) percentage: Percentage,
) {
    PercentageChange(
        percentage = percentage
    )

}

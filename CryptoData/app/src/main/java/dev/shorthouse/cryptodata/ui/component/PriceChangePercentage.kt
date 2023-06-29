package dev.shorthouse.cryptodata.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.ui.theme.NegativeRed
import dev.shorthouse.cryptodata.ui.theme.PositiveGreen

@Composable
fun PriceChangePercentage(
    priceChangePercentage: Double,
    modifier: Modifier = Modifier
) {
    val isPercentagePositive = priceChangePercentage >= 0

    val textColor = if (isPercentagePositive) {
        PositiveGreen
    } else {
        NegativeRed
    }

    val percentageFormatted = if (isPercentagePositive) {
        stringResource(R.string.positive_percentage, priceChangePercentage)
    } else {
        stringResource(R.string.negative_percentage, priceChangePercentage)
    }

    Text(
        text = percentageFormatted,
        color = textColor,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

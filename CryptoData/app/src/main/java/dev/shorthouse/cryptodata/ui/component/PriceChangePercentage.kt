package dev.shorthouse.cryptodata.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.ui.theme.NegativeRed
import dev.shorthouse.cryptodata.ui.theme.PositiveGreen

@Composable
fun PriceChangePercentage(
    priceChangePercentage: Percentage,
    modifier: Modifier = Modifier
) {
    val textColor = if (priceChangePercentage.amount >= 0) {
        PositiveGreen
    } else {
        NegativeRed
    }

    Text(
        text = priceChangePercentage.formattedAmount,
        color = textColor,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

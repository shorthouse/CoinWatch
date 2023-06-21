package dev.shorthouse.cryptodata.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    val icon = if (priceChangePercentage >= 0) {
        Icons.Rounded.ExpandLess
    } else {
        Icons.Rounded.ExpandMore
    }

    val textColor = if (priceChangePercentage >= 0) {
        PositiveGreen
    } else {
        NegativeRed
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor
        )
        Text(
            text = stringResource(
                id = R.string.coin_price_change,
                priceChangePercentage
            ),
            color = textColor
        )
    }
}

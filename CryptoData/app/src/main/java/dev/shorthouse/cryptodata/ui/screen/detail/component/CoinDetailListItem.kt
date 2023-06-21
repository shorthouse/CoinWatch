package dev.shorthouse.cryptodata.ui.screen.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.shorthouse.cryptodata.R
import dev.shorthouse.cryptodata.ui.component.PriceChangePercentage

@Composable
fun CoinDetailListItem(
    header: String,
    price: Double,
    priceChangePercentage: Double,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = header,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                text = stringResource(
                    id = R.string.coin_current_price,
                    price
                )
            )
        }

        PriceChangePercentage(
            priceChangePercentage = priceChangePercentage
        )
    }
}

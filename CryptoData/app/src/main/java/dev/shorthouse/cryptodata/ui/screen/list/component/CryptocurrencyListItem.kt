package dev.shorthouse.cryptodata.ui.screen.list.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.shorthouse.cryptodata.model.Cryptocurrency

@Composable
fun CryptocurrencyListItem(
    cryptocurrency: Cryptocurrency,
    onItemClick: (Cryptocurrency) -> Unit,
) {
    Row {
        Text(
            text = cryptocurrency.symbol,
        )
        Text(
            text = cryptocurrency.name,
        )
    }
}

package dev.shorthouse.cryptodata.ui.screen.detail.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.shorthouse.cryptodata.ui.theme.AppTheme

@Composable
fun CoinDetailListItem(
    header: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = header,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value
        )
    }
}

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun CoinDetailListItemPreview() {
    AppTheme {
        CoinDetailListItem(
            header = "header",
            value = "value",
        )
    }
}

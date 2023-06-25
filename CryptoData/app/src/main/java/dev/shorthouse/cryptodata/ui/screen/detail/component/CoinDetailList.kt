package dev.shorthouse.cryptodata.ui.screen.detail.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shorthouse.cryptodata.ui.theme.AppTheme

@Composable
fun CoinDetailList(
    title: String,
    items: List<CoinDetailListItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
        )

        Spacer(Modifier.height(4.dp))

        Surface(
            shadowElevation = 0.dp,
            tonalElevation = 0.dp,
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            ) {
                items.forEach { coinDetailListItem ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = coinDetailListItem.name,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = coinDetailListItem.value,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

data class CoinDetailListItem(
    val name: String,
    val value: String
)

@Composable
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun CoinDetailListPreview() {
    AppTheme {
        CoinDetailList(
            title = "Historic Data",
            items = listOf(
                CoinDetailListItem(
                    name = "All Time Low",
                    value = "$0.79"
                ),
                CoinDetailListItem(
                    name = "All Time High",
                    value = "$3260.39"
                ),
                CoinDetailListItem(
                    name = "All Time Low Date",
                    value = "10 October 2015"
                ),
                CoinDetailListItem(
                    name = "All Time High Date",
                    value = "22 May 2021"
                )

            )
        )
    }
}

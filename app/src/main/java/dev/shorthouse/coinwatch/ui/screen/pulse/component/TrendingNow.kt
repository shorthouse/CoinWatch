package dev.shorthouse.coinwatch.ui.screen.pulse.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.TrendingCoin
import dev.shorthouse.coinwatch.ui.preview.AppPreviewWrapper
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun TrendingNow(
    trendingCoins: ImmutableList<TrendingCoin>,
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spotlightCoin = trendingCoins.firstOrNull() ?: return
    val pillCoins = trendingCoins.drop(1)

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        TrendingSpotlightCard(
            trendingCoin = spotlightCoin,
            onCoinClick = onCoinClick
        )

        if (pillCoins.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = pillCoins,
                    key = { it.id }
                ) { trendingCoin ->
                    TrendingCoinPill(
                        trendingCoin = trendingCoin,
                        onCoinClick = onCoinClick
                    )
                }
            }
        }
    }
}

@Preview
@PreviewWrapper(wrapper = AppPreviewWrapper::class)
@Composable
private fun TrendingNowPreview() {
    TrendingNow(
        trendingCoins = List(6) { index ->
            TrendingCoin(
                id = "coin-$index",
                name = "Coin $index",
                symbol = "SYM$index",
                imageUrl = "",
                currentPrice = Price("${index + 1}.50"),
                priceChangePercentage24h = Percentage(if (index % 2 == 0) "5.5" else "-3.2"),
                sparkline = persistentListOf()
            )
        }.toPersistentList(),
        onCoinClick = {}
    )
}

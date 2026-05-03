package dev.shorthouse.coinwatch.model

import kotlinx.collections.immutable.ImmutableList

data class CoinChart(
    val priceHistory: ImmutableList<PriceEntry>,
    val periodPriceChangePercentage: Percentage
)

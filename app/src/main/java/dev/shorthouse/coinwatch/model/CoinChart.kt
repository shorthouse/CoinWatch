package dev.shorthouse.coinwatch.model

import java.math.BigDecimal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class CoinChart(
    val priceHistory: ImmutableList<PriceEntry>,
    val minPrice: Price,
    val maxPrice: Price,
    val periodPriceChangePercentage: Percentage
) {
    val prices: ImmutableList<BigDecimal> = priceHistory.map { it.price }.toImmutableList()
}

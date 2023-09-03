package dev.shorthouse.coinwatch.model

import java.math.BigDecimal
import kotlinx.collections.immutable.ImmutableList

data class CoinChart(
    val prices: ImmutableList<BigDecimal>,
    val minPrice: Price,
    val maxPrice: Price,
    val periodPriceChangePercentage: Percentage
)

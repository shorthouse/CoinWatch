package dev.shorthouse.coinwatch.model

import java.math.BigDecimal

data class CoinChart(
    val prices: List<BigDecimal>,
    val minPrice: Price,
    val minPriceChangePercentage: Percentage,
    val maxPrice: Price,
    val maxPriceChangePercentage: Percentage,
    val periodPriceChangePercentage: Percentage
)

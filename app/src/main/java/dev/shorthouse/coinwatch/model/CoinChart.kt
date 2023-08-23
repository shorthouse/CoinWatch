package dev.shorthouse.coinwatch.model

import java.math.BigDecimal

data class CoinChart(
    val prices: List<BigDecimal>,
    val minPrice: Price,
    val maxPrice: Price,
    val periodPriceChangePercentage: Percentage
)

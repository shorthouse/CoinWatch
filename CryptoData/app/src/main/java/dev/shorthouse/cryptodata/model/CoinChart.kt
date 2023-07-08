package dev.shorthouse.cryptodata.model

data class CoinChart(
    val prices: List<Double>,
    val minPrice: Price,
    val minPriceChangePercentage: Percentage,
    val maxPrice: Price,
    val maxPriceChangePercentage: Percentage,
    val periodPriceChangePercentage: Percentage
)

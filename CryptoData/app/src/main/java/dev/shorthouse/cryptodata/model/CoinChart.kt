package dev.shorthouse.cryptodata.model

data class CoinChart(
    val prices: List<Double>,
    val minPrice: Price,
    val minPriceChangePercentage: Double,
    val maxPrice: Price,
    val maxPriceChangePercentage: Double,
    val periodPriceChangePercentage: Double
)

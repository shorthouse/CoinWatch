package dev.shorthouse.cryptodata.model

data class CoinChart(
    val prices: List<Double>,
    val minPrice: Double,
    val minPriceChangePercentage: Double,
    val maxPrice: Double,
    val maxPriceChangePercentage: Double,
    val periodPriceChangePercentage: Double
)

package dev.shorthouse.cryptodata.model

data class CoinDetail(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: String,
    val marketCapRank: Int,
    val marketCap: String,
    val circulatingSupply: String,
    val allTimeLow: String,
    val allTimeHigh: String,
    val allTimeLowDate: String,
    val allTimeHighDate: String,
    val pastPrices: List<Double>,
    val minPrice: Double,
    val minPriceFormatted: String,
    val minPriceChangePercentage: Double,
    val maxPrice: Double,
    val maxPriceFormatted: String,
    val maxPriceChangePercentage: Double
)

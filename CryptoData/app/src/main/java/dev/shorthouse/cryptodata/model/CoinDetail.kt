package dev.shorthouse.cryptodata.model

data class CoinDetail(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: Price,
    val marketCapRank: String,
    val marketCap: Price,
    val circulatingSupply: String,
    val allTimeLow: Price,
    val allTimeHigh: Price,
    val allTimeLowDate: String,
    val allTimeHighDate: String,
    val pastPrices: List<Double>,
    val minPastPrice: Price,
    val minPriceChangePercentage: Percentage,
    val maxPastPrice: Price,
    val maxPriceChangePercentage: Percentage,
    val periodPriceChangePercentage: Percentage
)

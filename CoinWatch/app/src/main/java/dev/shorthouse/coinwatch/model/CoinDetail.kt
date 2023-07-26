package dev.shorthouse.coinwatch.model

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
    val allTimeHighDate: String
)

package dev.shorthouse.cryptodata.model

data class CoinDetail(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: Price,
    val marketCapRank: Int,
    val marketCap: Price,
    val circulatingSupply: String,
    val allTimeLow: Price,
    val allTimeHigh: Price,
    val allTimeLowDate: String,
    val allTimeHighDate: String
)

package dev.shorthouse.coinwatch.model

data class CoinDetail(
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val currentPrice: Price,
    val marketCap: Price,
    val marketCapRank: String,
    val volume24h: String,
    val circulatingSupply: String,
    val allTimeHigh: Price,
    val allTimeHighDate: String,
    val listedDate: String
)

package dev.shorthouse.coinwatch.model

data class CoinDetails(
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val currentPrice: Price,
    val marketCap: Price,
    val marketCapRank: String,
    val volume24h: Price,
    val circulatingSupply: String,
    val allTimeHigh: Price,
    val allTimeHighDate: String,
    val listedDate: String
)

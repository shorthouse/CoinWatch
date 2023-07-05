package dev.shorthouse.cryptodata.model

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: Price,
    val priceChangePercentage24h: Double,
    val marketCapRank: Int
)

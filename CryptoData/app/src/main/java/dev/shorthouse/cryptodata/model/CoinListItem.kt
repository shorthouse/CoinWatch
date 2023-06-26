package dev.shorthouse.cryptodata.model

data class CoinListItem(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double,
    val priceChangePercentage: Double
)

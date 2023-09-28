package dev.shorthouse.coinwatch.model

class SearchCoin(
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val currentPrice: Price
)

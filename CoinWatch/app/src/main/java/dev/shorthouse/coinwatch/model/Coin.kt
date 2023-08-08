package dev.shorthouse.coinwatch.model

import java.math.BigDecimal

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: Price,
    val priceChangePercentage24h: Percentage,
    val prices24h: List<BigDecimal>,
)

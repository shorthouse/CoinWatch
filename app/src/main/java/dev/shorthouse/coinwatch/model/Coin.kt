package dev.shorthouse.coinwatch.model

import java.math.BigDecimal
import kotlinx.collections.immutable.ImmutableList

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val currentPrice: Price,
    val priceChangePercentage24h: Percentage,
    val prices24h: ImmutableList<BigDecimal>
)

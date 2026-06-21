package dev.shorthouse.coinwatch.model

import kotlinx.collections.immutable.ImmutableList
import java.math.BigDecimal

data class Movers(
    val topGainer: MoverCoin,
    val topLoser: MoverCoin,
    val mostMovement: ImmutableList<MoverCoin>,
)

data class MoverCoin(
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val currentPrice: Price,
    val priceChangePercentage24h: Percentage,
    val sparkline: ImmutableList<BigDecimal>,
)

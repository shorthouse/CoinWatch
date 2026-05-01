package dev.shorthouse.coinwatch.model

import java.math.BigDecimal

data class PriceEntry(
    val price: BigDecimal,
    val timestamp: Long,
    val formattedDate: String
)

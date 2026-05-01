package dev.shorthouse.coinwatch.ui.model

import dev.shorthouse.coinwatch.model.Percentage
import java.math.BigDecimal

data class ScrubData(
    val price: BigDecimal,
    val timestamp: Long,
    val changePercentage: Percentage,
    val changeAmount: BigDecimal,
)
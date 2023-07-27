package dev.shorthouse.coinwatch.model

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

data class Percentage(private val amount: BigDecimal) {
    companion object {
        private val percentageFormat: NumberFormat =
            NumberFormat.getPercentInstance(Locale.US).apply {
                isGroupingUsed = true
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
    }

    val isPositive: Boolean = amount.signum() > 0
    val isNegative: Boolean = amount.signum() < 0

    val formattedAmount: String = if (isNegative) {
        percentageFormat.format(amount.divide(BigDecimal("100")))
    } else {
        "+" + percentageFormat.format(amount.divide(BigDecimal("100")))
    }
}

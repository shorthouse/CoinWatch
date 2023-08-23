package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrZero
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

data class Percentage(private val percentage: String?) {
    companion object {
        private val percentageFormat: NumberFormat =
            NumberFormat.getPercentInstance(Locale.US).apply {
                isGroupingUsed = true
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }
    }

    val amount: BigDecimal = percentage.toSanitisedBigDecimalOrZero()

    private val roundedAmount: BigDecimal = amount.setScale(2, RoundingMode.HALF_EVEN)
    val isPositive: Boolean = roundedAmount.signum() > 0
    val isNegative: Boolean = roundedAmount.signum() < 0

    val formattedAmount: String = if (isNegative) {
        percentageFormat.format(amount.divide(BigDecimal("100")))
    } else {
        "+" + percentageFormat.format(amount.divide(BigDecimal("100")))
    }
}

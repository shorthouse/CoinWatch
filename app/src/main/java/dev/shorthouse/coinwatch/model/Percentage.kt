package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrNull
import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrZero
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

data class Percentage(private val percentage: String?) {

    val amount: BigDecimal = percentage.toSanitisedBigDecimalOrZero()

    private val roundedAmount: BigDecimal = amount.setScale(2, RoundingMode.HALF_EVEN)
    val isPositive: Boolean = roundedAmount.signum() > 0
    val isNegative: Boolean = roundedAmount.signum() < 0

    val formattedAmount: String
        get() = formatAmount()

    private fun formatAmount(): String {
        val formatLocale = Locale.getDefault(Locale.Category.FORMAT)

        return if (percentage.toSanitisedBigDecimalOrNull() == null) {
            PercentageFormatter.formatMissing(formatLocale = formatLocale)
        } else {
            PercentageFormatter.format(
                amount = amount,
                formatLocale = formatLocale
            )
        }
    }
}

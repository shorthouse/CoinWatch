package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrZero
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

data class Price(private val price: String?) : Comparable<Price> {
    companion object {
        private val currencyFormat: NumberFormat = createCurrencyFormat(2)
        private val currencyFormatSmallPrice: NumberFormat = createCurrencyFormat(6)

        private fun createCurrencyFormat(fractionDigits: Int): NumberFormat {
            val format = DecimalFormat.getCurrencyInstance(Locale.US) as DecimalFormat
            format.currency = Currency.getInstance("USD")
            format.minimumFractionDigits = fractionDigits
            format.maximumFractionDigits = fractionDigits
            return format
        }

        private val minSmallValue: BigDecimal = BigDecimal("-1.00")
        private val maxSmallValue: BigDecimal = BigDecimal("1.00")
    }

    val amount: BigDecimal = price.toSanitisedBigDecimalOrZero()

    val formattedAmount: String = when {
        price == null -> "$ --"
        amount in (minSmallValue..maxSmallValue) -> currencyFormatSmallPrice.format(amount)
        else -> currencyFormat.format(amount)
    }

    override fun compareTo(other: Price): Int {
        return amount.compareTo(other.amount)
    }
}

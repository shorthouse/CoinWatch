package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrZero
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.Currency
import java.util.Locale

data class Price(private val price: String?) : Comparable<Price> {
    companion object {
        private val defaultFormat: DecimalFormat = createCurrencyFormat(decimalPlaces = 2)
        private val preciseFormat: DecimalFormat = createCurrencyFormat(decimalPlaces = 6)
        private val precisionThreshold = BigDecimal("-1.00")..BigDecimal("1.00")

        private fun createCurrencyFormat(decimalPlaces: Int): DecimalFormat {
            val currencyFormat = DecimalFormat.getCurrencyInstance(Locale.US) as DecimalFormat

            currencyFormat.currency = Currency.getInstance("USD")
            currencyFormat.minimumFractionDigits = decimalPlaces
            currencyFormat.maximumFractionDigits = decimalPlaces

            return currencyFormat
        }
    }

    val amount: BigDecimal = price.toSanitisedBigDecimalOrZero()

    val formattedAmount: String = when {
        price == null -> "$ --"
        amount in precisionThreshold -> preciseFormat.format(amount)
        else -> defaultFormat.format(amount)
    }

    override fun compareTo(other: Price): Int {
        return amount.compareTo(other.amount)
    }
}

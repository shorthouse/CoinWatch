package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrZero
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Locale
import java.util.Currency as CurrencyCode

data class Price(val price: String?, val currency: Currency = Currency.USD) : Comparable<Price> {
    val amount: BigDecimal = price.toSanitisedBigDecimalOrZero()

    private val currencyFormat: DecimalFormat = getCurrencyFormat()

    val formattedAmount: String = when {
        // Must be checked in this order
        price.isNullOrBlank() -> "${currency.symbol}--"
        amount in belowOneRange -> currencyFormat.format(amount)
        amount in belowMillionRange -> currencyFormat.format(amount)
        else -> formatLargeAmount()
    }

    private fun getCurrencyFormat(): DecimalFormat {
        val currencyFormat = DecimalFormat.getCurrencyInstance(Locale.US) as DecimalFormat

        val decimalPlaces = if (amount in belowOneRange) 6 else 2
        val currencyCode = try {
            CurrencyCode.getInstance(currency.name)
        } catch (e: IllegalArgumentException) {
            CurrencyCode.getInstance(Currency.USD.name)
        }

        currencyFormat.minimumFractionDigits = decimalPlaces
        currencyFormat.maximumFractionDigits = decimalPlaces
        currencyFormat.currency = currencyCode

        return currencyFormat
    }

    private fun formatLargeAmount(): String {
        val roundedAmount = amount.round(MathContext(5, roundingMode))

        val divisor = when (roundedAmount) {
            in millionRange -> million
            in billionRange -> billion
            in trillionRange -> trillion
            in quadrillionRange -> quadrillion
            else -> BigDecimal.ONE
        }

        val symbol = when (roundedAmount) {
            in millionRange -> "M"
            in billionRange -> "B"
            in trillionRange -> "T"
            in quadrillionRange -> "Q"
            else -> ""
        }

        val shortenedAmount = amount.divide(divisor, 2, roundingMode)
        return currencyFormat.format(shortenedAmount) + symbol
    }

    override fun compareTo(other: Price) = amount.compareTo(other.amount)

    companion object {
        private val million = BigDecimal("1000000")
        private val billion = BigDecimal("1000000000")
        private val trillion = BigDecimal("1000000000000")
        private val quadrillion = BigDecimal("1000000000000000")
        private val quintillion = BigDecimal("1000000000000000000")

        private val belowOneRange = BigDecimal("-1.00")..BigDecimal("1.00")
        private val belowMillionRange = BigDecimal("1.00")..<million
        private val millionRange = million..<billion
        private val billionRange = billion..<trillion
        private val trillionRange = trillion..<quadrillion
        private val quadrillionRange = quadrillion..<quintillion

        val roundingMode = RoundingMode.HALF_EVEN
    }
}

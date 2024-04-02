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

    private val currencyFormat by lazy {
        val currencyFormat = DecimalFormat.getCurrencyInstance(Locale.US) as DecimalFormat

        val decimalPlaces = if (amount in belowOneThreshold) 6 else 2
        val currencyCode = try {
            CurrencyCode.getInstance(currency.name)
        } catch (e: IllegalArgumentException) {
            CurrencyCode.getInstance(Currency.USD.name)
        }

        currencyFormat.minimumFractionDigits = decimalPlaces
        currencyFormat.maximumFractionDigits = decimalPlaces
        currencyFormat.currency = currencyCode

        currencyFormat
    }

    val formattedAmount: String by lazy {
        when {
            price.isNullOrBlank() -> "${currency.symbol}--"
            amount in belowOneThreshold -> currencyFormat.format(amount)
            amount in smallThreshold -> currencyFormat.format(amount)
            else -> formatLargeAmount()
        }
    }

    private fun formatLargeAmount(): String {
        val roundedAmount = amount.round(MathContext(5, roundingMode))

        val divisor = when (roundedAmount) {
            in millionThreshold -> million
            in billionThreshold -> billion
            in trillionThreshold -> trillion
            in quadrillionThreshold -> quadrillion
            else -> BigDecimal.ONE
        }

        val symbol = when (roundedAmount) {
            in millionThreshold -> "M"
            in billionThreshold -> "B"
            in trillionThreshold -> "T"
            in quadrillionThreshold -> "Q"
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

        private val belowOneThreshold = BigDecimal("-1.00")..BigDecimal("1.00")
        private val smallThreshold = BigDecimal("1.00")..<million
        private val millionThreshold = million..<billion
        private val billionThreshold = billion..<trillion
        private val trillionThreshold = trillion..<quadrillion
        private val quadrillionThreshold = quadrillion..<quintillion

        val roundingMode = RoundingMode.HALF_EVEN
    }
}

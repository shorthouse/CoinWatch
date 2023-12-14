package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrZero
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.Currency as CurrencyCode
import java.util.Locale

data class Price(val price: String?, val currency: Currency = Currency.USD) : Comparable<Price> {
    companion object {
        private val precisionThreshold = BigDecimal("-1.00")..BigDecimal("1.00")

        private fun createCurrencyFormat(decimalPlaces: Int, currency: Currency): DecimalFormat {
            val currencyFormat = DecimalFormat.getCurrencyInstance(Locale.US) as DecimalFormat

            currencyFormat.minimumFractionDigits = decimalPlaces
            currencyFormat.maximumFractionDigits = decimalPlaces

            val currencyCode = try {
                CurrencyCode.getInstance(currency.name)
            } catch (e: IllegalArgumentException) {
                CurrencyCode.getInstance(Currency.USD.name)
            }
            currencyFormat.currency = currencyCode

            return currencyFormat
        }
    }

    val amount: BigDecimal = price.toSanitisedBigDecimalOrZero()

    private val currencyFormat = if (amount in precisionThreshold) {
        createCurrencyFormat(decimalPlaces = 6, currency = currency)
    } else {
        createCurrencyFormat(decimalPlaces = 2, currency = currency)
    }

    val formattedAmount: String = when (price) {
        null -> "${currency.symbol} --"
        else -> currencyFormat.format(amount)
    }

    override fun compareTo(other: Price): Int {
        return amount.compareTo(other.amount)
    }
}

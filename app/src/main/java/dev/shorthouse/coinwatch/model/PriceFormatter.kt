package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Locale
import java.util.Currency as CurrencyCode

internal object PriceFormatter {
    fun format(amount: BigDecimal, currency: Currency): String {
        val currencyFormat = getCurrencyFormat(currency, amount = amount)
        val abbreviation = findAbbreviationOrNull(amount)

        return if (abbreviation == null) {
            currencyFormat.format(amount)
        } else {
            formatAbbreviatedAmount(amount, abbreviation, currencyFormat)
        }
    }

    private fun findAbbreviationOrNull(amount: BigDecimal): PriceAbbreviation? {
        if (amount < million) {
            return null
        }

        val roundedAmount = amount.round(MathContext(5, roundingMode))
        return abbreviations.firstOrNull { it.contains(roundedAmount) }
    }

    private fun formatAbbreviatedAmount(
        amount: BigDecimal,
        abbreviation: PriceAbbreviation,
        currencyFormat: DecimalFormat,
    ): String {
        val shortenedAmount = amount.divide(abbreviation.divisor, 2, roundingMode)
        return currencyFormat.format(shortenedAmount) + abbreviation.suffix
    }

    private fun getCurrencyFormat(currency: Currency, amount: BigDecimal): DecimalFormat {
        val currencyFormat = DecimalFormat.getCurrencyInstance(Locale.US) as DecimalFormat
        val currencyCode = try {
            CurrencyCode.getInstance(currency.name)
        } catch (e: IllegalArgumentException) {
            CurrencyCode.getInstance(Currency.USD.name)
        }

        val decimalPlaces = if (amount in smallPriceLowerBound..smallPriceUpperBound) 6 else 2

        currencyFormat.minimumFractionDigits = decimalPlaces
        currencyFormat.maximumFractionDigits = decimalPlaces
        currencyFormat.currency = currencyCode

        return currencyFormat
    }

    private data class PriceAbbreviation(
        val divisor: BigDecimal,
        val exclusiveUpperBound: BigDecimal,
        val suffix: String,
    ) {
        fun contains(amount: BigDecimal): Boolean {
            return amount in divisor..<exclusiveUpperBound
        }
    }

    private val million = BigDecimal("1000000")
    private val billion = BigDecimal("1000000000")
    private val trillion = BigDecimal("1000000000000")
    private val quadrillion = BigDecimal("1000000000000000")
    private val quintillion = BigDecimal("1000000000000000000")

    private val smallPriceLowerBound = BigDecimal("-1.00")
    private val smallPriceUpperBound = BigDecimal("1.00")

    private val abbreviations = listOf(
        PriceAbbreviation(divisor = million, exclusiveUpperBound = billion, suffix = "M"),
        PriceAbbreviation(divisor = billion, exclusiveUpperBound = trillion, suffix = "B"),
        PriceAbbreviation(divisor = trillion, exclusiveUpperBound = quadrillion, suffix = "T"),
        PriceAbbreviation(divisor = quadrillion, exclusiveUpperBound = quintillion, suffix = "Q")
    )

    private val roundingMode = RoundingMode.HALF_EVEN
}

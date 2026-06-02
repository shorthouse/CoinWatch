package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.Constants.MISSING_VALUE_PLACEHOLDER
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.AttributedCharacterIterator
import java.text.CharacterIterator
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import java.util.Currency as JavaCurrency

internal object PriceFormatter {
    fun format(amount: BigDecimal, currency: Currency, formatLocale: Locale): String {
        val abbreviation = findAbbreviationOrNull(amount)

        return if (abbreviation == null) {
            val currencyFormat = createCurrencyFormat(
                currency = currency,
                formatLocale = formatLocale,
                decimalPlaces = fractionDigitsFor(amount)
            )
            currencyFormat.format(amount)
        } else {
            val shortenedAmount = amount.divide(abbreviation.divisor, 2, roundingMode)
            val currencyFormat = createCurrencyFormat(
                currency = currency,
                formatLocale = formatLocale,
                decimalPlaces = 2
            )
            insertAbbreviationSuffix(currencyFormat, shortenedAmount, abbreviation.suffix)
        }
    }

    fun formatMissing(currency: Currency, formatLocale: Locale): String {
        val currencyFormat = createCurrencyFormat(
            currency = currency,
            formatLocale = formatLocale,
            decimalPlaces = 2
        )
        return withNumericSpan(currencyFormat, BigDecimal.ZERO) { formatted, start, end ->
            formatted.substring(0, start) + MISSING_VALUE_PLACEHOLDER + formatted.substring(end)
        } ?: defaultMissingFallback(currency, formatLocale)
    }

    private fun insertAbbreviationSuffix(
        format: DecimalFormat,
        amount: BigDecimal,
        suffix: String,
    ): String {
        return withNumericSpan(format, amount) { formatted, _, end ->
            formatted.substring(0, end) + suffix + formatted.substring(end)
        } ?: (format.format(amount) + suffix)
    }

    private fun findAbbreviationOrNull(amount: BigDecimal): PriceAbbreviation? {
        if (amount < million) {
            return null
        }
        val roundedAmount = amount.round(tierRoundingContext)
        return abbreviations.firstOrNull { it.contains(roundedAmount) }
    }

    private fun createCurrencyFormat(
        currency: Currency,
        formatLocale: Locale,
        decimalPlaces: Int,
    ): DecimalFormat {
        val currencyFormat = NumberFormat.getCurrencyInstance(formatLocale) as DecimalFormat

        currencyFormat.minimumFractionDigits = decimalPlaces
        currencyFormat.maximumFractionDigits = decimalPlaces
        currencyFormat.currency = toJavaCurrency(currency)

        return currencyFormat
    }

    private fun toJavaCurrency(currency: Currency): JavaCurrency {
        return try {
            JavaCurrency.getInstance(currency.name)
        } catch (e: IllegalArgumentException) {
            JavaCurrency.getInstance(Currency.USD.name)
        }
    }

    private fun withNumericSpan(
        format: DecimalFormat,
        value: BigDecimal,
        transform: (formatted: String, start: Int, end: Int) -> String,
    ): String? {
        val iter = format.formatToCharacterIterator(value)
        val formatted = StringBuilder()
        var spanStart = -1
        var spanEnd = -1

        var char = iter.first()
        while (char != CharacterIterator.DONE) {
            val isNumeric = iter.attributes.keys.any { it in numericFields }
            if (isNumeric) {
                if (spanStart == -1) spanStart = formatted.length
                spanEnd = formatted.length + 1
            }
            formatted.append(char)
            char = iter.next()
        }

        if (spanStart == -1 || spanEnd == -1) return null
        return transform(formatted.toString(), spanStart, spanEnd)
    }

    private fun defaultMissingFallback(currency: Currency, formatLocale: Locale): String {
        return toJavaCurrency(currency).getSymbol(formatLocale) + MISSING_VALUE_PLACEHOLDER
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

    private fun fractionDigitsFor(amount: BigDecimal): Int =
        if (amount in smallPriceLowerBound..smallPriceUpperBound) 6 else 2

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

    private val numericFields = setOf<AttributedCharacterIterator.Attribute>(
        NumberFormat.Field.INTEGER,
        NumberFormat.Field.FRACTION,
        NumberFormat.Field.DECIMAL_SEPARATOR,
        NumberFormat.Field.GROUPING_SEPARATOR,
    )

    private val roundingMode = RoundingMode.HALF_EVEN

    // Precision used to decide which abbreviation tier a value falls into.
    // 5 significant figures means values like 999,995,000 round up into the
    // next tier (e.g. into "$1.00B" rather than staying in "$999.99M").
    private val tierRoundingContext = MathContext(5, roundingMode)
}

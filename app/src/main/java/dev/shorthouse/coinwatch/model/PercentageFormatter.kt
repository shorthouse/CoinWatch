package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.common.Constants.MISSING_VALUE_PLACEHOLDER
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.AttributedCharacterIterator
import java.text.CharacterIterator
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

internal object PercentageFormatter {
    fun format(amount: BigDecimal, formatLocale: Locale): String {
        val percentFormat = createPercentFormat(formatLocale)
        val fraction = amount.divide(oneHundred)

        return if (isNegative(amount)) {
            percentFormat.format(fraction)
        } else {
            insertPositiveSign(format = percentFormat, fraction = fraction.abs())
        }
    }

    fun formatUnsigned(amount: BigDecimal, formatLocale: Locale): String {
        val percentFormat = createPercentFormat(formatLocale)
        val fraction = amount.divide(oneHundred)

        return percentFormat.format(fraction)
    }

    fun formatMissing(formatLocale: Locale): String {
        val percentFormat = createPercentFormat(formatLocale)
        return withNumericSpan(percentFormat, BigDecimal.ZERO) { formatted, start, end ->
            formatted.substring(0, start) + MISSING_VALUE_PLACEHOLDER + formatted.substring(end)
        } ?: defaultMissingFallback(percentFormat)
    }

    private fun insertPositiveSign(format: DecimalFormat, fraction: BigDecimal): String {
        return withNumericSpan(format, fraction) { formatted, start, _ ->
            formatted.substring(0, start) + POSITIVE_SIGN + formatted.substring(start)
        } ?: (POSITIVE_SIGN + format.format(fraction))
    }

    private fun createPercentFormat(formatLocale: Locale): DecimalFormat {
        val percentFormat = NumberFormat.getPercentInstance(formatLocale) as DecimalFormat

        percentFormat.isGroupingUsed = true
        percentFormat.minimumFractionDigits = 2
        percentFormat.maximumFractionDigits = 2

        return percentFormat
    }

    private fun isNegative(amount: BigDecimal): Boolean =
        amount.setScale(2, RoundingMode.HALF_EVEN).signum() < 0

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

    private fun defaultMissingFallback(format: DecimalFormat): String {
        return MISSING_VALUE_PLACEHOLDER + format.decimalFormatSymbols.percent
    }

    private val oneHundred = BigDecimal("100")

    private const val POSITIVE_SIGN = "+"

    private val numericFields = setOf<AttributedCharacterIterator.Attribute>(
        NumberFormat.Field.INTEGER,
        NumberFormat.Field.FRACTION,
        NumberFormat.Field.DECIMAL_SEPARATOR,
        NumberFormat.Field.GROUPING_SEPARATOR,
    )
}

package dev.shorthouse.coinwatch.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

class PercentageFormatterTest {

    @Test
    fun `en-US formats positive percentage with plus sign`() {
        assertThat(PercentageFormatter.format(BigDecimal("1.23"), Locale.US))
            .isEqualTo("+1.23%")
    }

    @Test
    fun `en-US formats negative percentage with minus sign`() {
        assertThat(PercentageFormatter.format(BigDecimal("-1.23"), Locale.US))
            .isEqualTo("-1.23%")
    }

    @Test
    fun `en-US formats zero percentage with plus sign`() {
        assertThat(PercentageFormatter.format(BigDecimal("0"), Locale.US))
            .isEqualTo("+0.00%")
    }

    @Test
    fun `en-US formats negative percentage that rounds to zero as positive zero`() {
        assertThat(PercentageFormatter.format(BigDecimal("-0.001"), Locale.US))
            .isEqualTo("+0.00%")
    }

    @Test
    fun `en-US formats large percentage with grouping`() {
        assertThat(PercentageFormatter.format(BigDecimal("1234567.89"), Locale.US))
            .isEqualTo("+1,234,567.89%")
    }

    @Test
    fun `en-US formats placeholder`() {
        assertThat(PercentageFormatter.formatMissing(Locale.US)).isEqualTo("—%")
    }

    @Test
    fun `en-US formats unsigned percentage without plus sign`() {
        val formatted = PercentageFormatter.formatUnsigned(BigDecimal("54.2"), Locale.US)

        assertThat(formatted).isEqualTo("54.20%")
        assertThat(formatted).doesNotContain("+")
    }

    @Test
    fun `en-GB formats positive percentage with plus sign`() {
        assertThat(PercentageFormatter.format(BigDecimal("1.23"), Locale.UK))
            .isEqualTo("+1.23%")
    }

    @Test
    fun `en-GB formats negative percentage with minus sign`() {
        assertThat(PercentageFormatter.format(BigDecimal("-1.23"), Locale.UK))
            .isEqualTo("-1.23%")
    }

    @Test
    fun `en-GB formats large percentage with grouping`() {
        assertThat(PercentageFormatter.format(BigDecimal("1234567.89"), Locale.UK))
            .isEqualTo("+1,234,567.89%")
    }

    @Test
    fun `en-GB formats placeholder`() {
        assertThat(PercentageFormatter.formatMissing(Locale.UK)).isEqualTo("—%")
    }

    @Test
    fun `de-DE uses comma decimal separator`() {
        val de = Locale.GERMANY
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), de)
        assertThat(formatted).contains("${decimalSeparator(de)}23")
    }

    @Test
    fun `de-DE uses locale grouping for large percentages`() {
        val de = Locale.GERMANY
        val formatted = PercentageFormatter.format(BigDecimal("1234567.89"), de)
        assertThat(formatted).contains(groupedInteger(de))
        assertThat(formatted).contains("${decimalSeparator(de)}89")
    }

    @Test
    fun `de-DE places percent symbol after digits`() {
        val de = Locale.GERMANY
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), de)
        assertPercentAfterDigits(formatted, de)
    }

    @Test
    fun `de-DE positive sign sits adjacent to leading digit`() {
        val de = Locale.GERMANY
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), de)
        assertPositiveSignAdjacentToDigit(formatted)
    }

    @Test
    fun `de-DE negative percentage keeps minus sign and omits plus`() {
        val de = Locale.GERMANY
        val formatted = PercentageFormatter.format(BigDecimal("-1.23"), de)
        assertThat(formatted).startsWith("-")
        assertThat(formatted).doesNotContain("+")
    }

    @Test
    fun `de-DE placeholder places em-dash before percent symbol`() {
        val formatted = PercentageFormatter.formatMissing(Locale.GERMANY)
        assertPlaceholderBeforePercent(formatted, Locale.GERMANY)
    }

    @Test
    fun `de-DE formats unsigned percentage with comma decimal separator`() {
        val de = Locale.GERMANY
        val formatted = PercentageFormatter.formatUnsigned(BigDecimal("54.2"), de)

        assertThat(formatted).contains("54${decimalSeparator(de)}20")
        assertThat(formatted).doesNotContain("+")
    }

    @Test
    fun `fr-FR uses comma decimal separator`() {
        val fr = Locale.FRANCE
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), fr)
        assertThat(formatted).contains("${decimalSeparator(fr)}23")
    }

    @Test
    fun `fr-FR uses locale grouping for large percentages`() {
        val fr = Locale.FRANCE
        val formatted = PercentageFormatter.format(BigDecimal("1234567.89"), fr)
        assertThat(formatted).contains(groupedInteger(fr))
    }

    @Test
    fun `fr-FR places percent symbol after digits`() {
        val fr = Locale.FRANCE
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), fr)
        assertPercentAfterDigits(formatted, fr)
    }

    @Test
    fun `fr-FR positive sign sits adjacent to leading digit`() {
        val fr = Locale.FRANCE
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), fr)
        assertPositiveSignAdjacentToDigit(formatted)
    }

    @Test
    fun `fr-FR placeholder places em-dash before percent symbol`() {
        val formatted = PercentageFormatter.formatMissing(Locale.FRANCE)
        assertPlaceholderBeforePercent(formatted, Locale.FRANCE)
    }

    @Test
    fun `tr-TR uses comma decimal separator`() {
        val tr = Locale.forLanguageTag("tr-TR")
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), tr)
        assertThat(formatted).contains("${decimalSeparator(tr)}23")
    }

    @Test
    fun `tr-TR uses locale grouping for large percentages`() {
        val tr = Locale.forLanguageTag("tr-TR")
        val formatted = PercentageFormatter.format(BigDecimal("1234567.89"), tr)
        assertThat(formatted).contains(groupedInteger(tr))
    }

    @Test
    fun `tr-TR places percent symbol before digits`() {
        val tr = Locale.forLanguageTag("tr-TR")
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), tr)
        assertPercentBeforeDigits(formatted, tr)
    }

    @Test
    fun `tr-TR positive sign sits adjacent to leading digit`() {
        val tr = Locale.forLanguageTag("tr-TR")
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), tr)
        assertPositiveSignAdjacentToDigit(formatted)
    }

    @Test
    fun `tr-TR placeholder places percent symbol before em-dash`() {
        val tr = Locale.forLanguageTag("tr-TR")
        val formatted = PercentageFormatter.formatMissing(tr)
        val placeholderIndex = formatted.indexOf('—')
        val percentIndex = percentIndex(formatted, tr)

        assertThat(percentIndex).isAtLeast(0)
        assertThat(placeholderIndex).isGreaterThan(percentIndex)
    }

    @Test
    fun `tr-TR formats unsigned percentage with percent symbol before digits`() {
        val tr = Locale.forLanguageTag("tr-TR")
        val formatted = PercentageFormatter.formatUnsigned(BigDecimal("54.2"), tr)

        assertPercentBeforeDigits(formatted, tr)
        assertThat(formatted).doesNotContain("+")
    }

    @Test
    fun `ar-SA uses Arabic-Indic digits`() {
        val ar = Locale.forLanguageTag("ar-SA")
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), ar)
        assertThat(formatted.any { it in '٠'..'٩' }).isTrue()
    }

    @Test
    fun `ar-SA includes percent representation`() {
        val ar = Locale.forLanguageTag("ar-SA")
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), ar)
        assertThat(formatted).contains(percentRepresentation(ar))
    }

    @Test
    fun `ar-SA positive sign sits adjacent to leading digit`() {
        val ar = Locale.forLanguageTag("ar-SA")
        val formatted = PercentageFormatter.format(BigDecimal("1.23"), ar)
        assertPositiveSignAdjacentToDigit(formatted)
    }

    @Test
    fun `ar-SA negative percentage keeps minus sign and omits plus`() {
        val ar = Locale.forLanguageTag("ar-SA")
        val formatted = PercentageFormatter.format(BigDecimal("-1.23"), ar)
        assertThat(formatted).contains(minusSign(ar))
        assertThat(formatted).doesNotContain("+")
    }

    @Test
    fun `ar-SA formats unsigned percentage with localized digits and percent representation`() {
        val ar = Locale.forLanguageTag("ar-SA")
        val formatted = PercentageFormatter.formatUnsigned(BigDecimal("54.2"), ar)

        assertThat(formatted.any { it in '٠'..'٩' }).isTrue()
        assertThat(formatted).contains(percentRepresentation(ar))
        assertThat(formatted).doesNotContain("+")
    }

    @Test
    fun `all locales place positive sign adjacent to leading digit`() {
        allLocales.forEach { locale ->
            val formatted = PercentageFormatter.format(BigDecimal("1.23"), locale)
            assertPositiveSignAdjacentToDigit(formatted)
        }
    }

    @Test
    fun `all locales negative percentage keeps minus sign and omits plus`() {
        allLocales.forEach { locale ->
            val formatted = PercentageFormatter.format(BigDecimal("-1.23"), locale)
            assertThat(formatted).doesNotContain("+")
            assertThat(formatted).contains(minusSign(locale))
        }
    }

    @Test
    fun `all locales placeholder has single em-dash no digits and percent representation`() {
        allLocales.forEach { locale ->
            val formatted = PercentageFormatter.formatMissing(locale)

            assertThat(formatted.count { it == '—' }).isEqualTo(1)
            assertThat(formatted.none { it.isDigit() }).isTrue()
            assertThat(formatted).contains(percentRepresentation(locale))
        }
    }

    @Test
    fun `symbol-after locales place placeholder before percent representation`() {
        listOf(
            Locale.US,
            Locale.UK,
            Locale.GERMANY,
            Locale.FRANCE,
            Locale.forLanguageTag("ar-SA"),
        ).forEach { locale ->
            val formatted = PercentageFormatter.formatMissing(locale)
            assertPlaceholderBeforePercent(formatted, locale)
        }
    }

    private val allLocales = listOf(
        Locale.US,
        Locale.UK,
        Locale.GERMANY,
        Locale.FRANCE,
        Locale.forLanguageTag("tr-TR"),
        Locale.forLanguageTag("ar-SA"),
    )

    private fun assertPositiveSignAdjacentToDigit(formatted: String) {
        val plusIndex = formatted.indexOf('+')

        assertThat(plusIndex).isAtLeast(0)
        assertThat(formatted[plusIndex + 1].isDigit()).isTrue()
    }

    private fun assertPercentAfterDigits(formatted: String, locale: Locale) {
        val percentIndex = percentIndex(formatted, locale)
        val lastDigitIndex = formatted.indexOfLast { it.isDigit() }

        assertThat(lastDigitIndex).isAtLeast(0)
        assertThat(percentIndex).isGreaterThan(lastDigitIndex)
    }

    private fun assertPercentBeforeDigits(formatted: String, locale: Locale) {
        val percentIndex = percentIndex(formatted, locale)
        val firstDigitIndex = formatted.indexOfFirst { it.isDigit() }

        assertThat(percentIndex).isAtLeast(0)
        assertThat(firstDigitIndex).isGreaterThan(percentIndex)
    }

    private fun assertPlaceholderBeforePercent(formatted: String, locale: Locale) {
        val placeholderIndex = formatted.indexOf('—')
        val percentIndex = percentIndex(formatted, locale)

        assertThat(placeholderIndex).isAtLeast(0)
        assertThat(percentIndex).isGreaterThan(placeholderIndex)
    }

    private fun percentIndex(formatted: String, locale: Locale): Int =
        formatted.indexOf(symbolsFor(locale).percent)

    private fun groupedInteger(locale: Locale): String {
        val grouping = symbolsFor(locale).groupingSeparator
        return "1${grouping}234${grouping}567"
    }

    private fun decimalSeparator(locale: Locale): Char = symbolsFor(locale).decimalSeparator

    private fun percentRepresentation(locale: Locale): String = symbolsFor(locale).percent.toString()

    private fun minusSign(locale: Locale): String = symbolsFor(locale).minusSign.toString()

    private fun symbolsFor(locale: Locale): DecimalFormatSymbols =
        (NumberFormat.getPercentInstance(locale) as DecimalFormat).decimalFormatSymbols
}

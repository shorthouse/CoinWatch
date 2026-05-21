package dev.shorthouse.coinwatch.model

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import org.junit.Test
import java.math.BigDecimal
import java.util.Locale
import java.util.Currency as JavaCurrency

class PriceFormatterTest {
    @Test
    fun `en-US formats normal amount with 2 decimal places`() {
        assertThat(PriceFormatter.format(BigDecimal("1.23"), Currency.USD, Locale.US))
            .isEqualTo("$1.23")
    }

    @Test
    fun `en-US formats negative amount with 2 decimal places`() {
        assertThat(PriceFormatter.format(BigDecimal("-1.23"), Currency.USD, Locale.US))
            .isEqualTo("-$1.23")
    }

    @Test
    fun `en-US formats small positive amount with 6 decimal places`() {
        assertThat(PriceFormatter.format(BigDecimal("0.000123"), Currency.USD, Locale.US))
            .isEqualTo("$0.000123")
    }

    @Test
    fun `en-US formats small negative amount with 6 decimal places`() {
        assertThat(PriceFormatter.format(BigDecimal("-0.009232"), Currency.USD, Locale.US))
            .isEqualTo("-$0.009232")
    }

    @Test
    fun `en-US formats grouping under million`() {
        assertThat(PriceFormatter.format(BigDecimal("29446.34"), Currency.USD, Locale.US))
            .isEqualTo("$29,446.34")
    }

    @Test
    fun `en-US formats million abbreviation`() {
        assertThat(PriceFormatter.format(BigDecimal("49491394"), Currency.USD, Locale.US))
            .isEqualTo("$49.49M")
    }

    @Test
    fun `en-US formats billion abbreviation`() {
        assertThat(PriceFormatter.format(BigDecimal("1009900243"), Currency.USD, Locale.US))
            .isEqualTo("$1.01B")
    }

    @Test
    fun `en-US formats trillion abbreviation`() {
        assertThat(PriceFormatter.format(BigDecimal("3084938574102"), Currency.USD, Locale.US))
            .isEqualTo("$3.08T")
    }

    @Test
    fun `en-US formats USD placeholder`() {
        assertThat(PriceFormatter.formatMissing(Currency.USD, Locale.US)).isEqualTo("$—")
    }

    @Test
    fun `en-US formats EUR placeholder`() {
        assertThat(PriceFormatter.formatMissing(Currency.EUR, Locale.US)).isEqualTo("€—")
    }

    @Test
    fun `en-US formats GBP placeholder`() {
        assertThat(PriceFormatter.formatMissing(Currency.GBP, Locale.US)).isEqualTo("£—")
    }

    @Test
    fun `en-GB formats USD with disambiguated symbol`() {
        assertThat(PriceFormatter.format(BigDecimal("1234.56"), Currency.USD, Locale.UK))
            .isEqualTo("US\$1,234.56")
    }

    @Test
    fun `en-GB formats USD placeholder with disambiguated symbol`() {
        assertThat(PriceFormatter.formatMissing(Currency.USD, Locale.UK)).isEqualTo("US\$—")
    }

    @Test
    fun `de-DE formats USD million abbreviation with M adjacent to digits`() {
        val expected = "49,49M \$"
        assertThat(PriceFormatter.format(BigDecimal("49491394"), Currency.USD, Locale.GERMANY))
            .isEqualTo(expected)
    }

    @Test
    fun `de-DE formats USD billion abbreviation with B adjacent to digits`() {
        val expected = "1,01B \$"
        assertThat(PriceFormatter.format(BigDecimal("1009900243"), Currency.USD, Locale.GERMANY))
            .isEqualTo(expected)
    }

    @Test
    fun `de-DE formats USD placeholder with em-dash left of symbol`() {
        val expected = "— \$"
        assertThat(PriceFormatter.formatMissing(Currency.USD, Locale.GERMANY)).isEqualTo(expected)
    }

    @Test
    fun `de-DE normal amount uses comma decimal and period grouping`() {
        val formatted = PriceFormatter.format(BigDecimal("1234.56"), Currency.USD, Locale.GERMANY)
        assertThat(formatted).contains(",56")
        assertThat(formatted).contains("1.234")
    }

    @Test
    fun `fr-FR normal amount uses comma decimal`() {
        val formatted = PriceFormatter.format(BigDecimal("1234.56"), Currency.USD, Locale.FRANCE)
        assertThat(formatted).contains(",56")
    }

    @Test
    fun `tr-TR normal amount uses comma decimal and period grouping`() {
        val tr = Locale.forLanguageTag("tr-TR")
        val formatted = PriceFormatter.format(BigDecimal("1234.56"), Currency.USD, tr)
        assertThat(formatted).contains(",56")
        assertThat(formatted).contains("1.234")
    }

    @Test
    fun `de-DE abbreviation places suffix adjacent to last digit not symbol`() {
        val formatted = PriceFormatter.format(BigDecimal("49491394"), Currency.USD, Locale.GERMANY)
        val mIndex = formatted.indexOf('M')
        assertThat(mIndex).isGreaterThan(0)
        assertThat(formatted[mIndex - 1].isDigit()).isTrue()
    }

    @Test
    fun `fr-FR abbreviation places suffix adjacent to last digit not symbol`() {
        val formatted = PriceFormatter.format(BigDecimal("49491394"), Currency.USD, Locale.FRANCE)
        val mIndex = formatted.indexOf('M')
        assertThat(mIndex).isGreaterThan(0)
        assertThat(formatted[mIndex - 1].isDigit()).isTrue()
    }

    @Test
    fun `tr-TR abbreviation places suffix adjacent to last digit not symbol`() {
        val tr = Locale.forLanguageTag("tr-TR")
        val formatted = PriceFormatter.format(BigDecimal("49491394"), Currency.USD, tr)
        val mIndex = formatted.indexOf('M')
        assertThat(mIndex).isGreaterThan(0)
        assertThat(formatted[mIndex - 1].isDigit()).isTrue()
    }

    @Test
    fun `de-DE billion abbreviation places B adjacent to last digit`() {
        val formatted = PriceFormatter.format(BigDecimal("1009900243"), Currency.USD, Locale.GERMANY)
        val bIndex = formatted.indexOf('B')
        assertThat(bIndex).isGreaterThan(0)
        assertThat(formatted[bIndex - 1].isDigit()).isTrue()
    }

    @Test
    fun `de-DE placeholder contains single em-dash`() {
        val formatted = PriceFormatter.formatMissing(Currency.USD, Locale.GERMANY)
        assertThat(formatted.count { it == '—' }).isEqualTo(1)
    }

    @Test
    fun `fr-FR placeholder contains single em-dash`() {
        val formatted = PriceFormatter.formatMissing(Currency.USD, Locale.FRANCE)
        assertThat(formatted.count { it == '—' }).isEqualTo(1)
    }

    @Test
    fun `tr-TR placeholder contains single em-dash`() {
        val tr = Locale.forLanguageTag("tr-TR")
        val formatted = PriceFormatter.formatMissing(Currency.USD, tr)
        assertThat(formatted.count { it == '—' }).isEqualTo(1)
    }

    @Test
    fun `de-DE small price uses 6 decimal places`() {
        val formatted = PriceFormatter.format(BigDecimal("0.000123"), Currency.USD, Locale.GERMANY)
        assertThat(formatted).contains(",000123")
    }

    @Test
    fun `de-DE negative amount places minus before number`() {
        val formatted = PriceFormatter.format(BigDecimal("-1234.56"), Currency.USD, Locale.GERMANY)
        assertThat(formatted).startsWith("-")
    }

    @Test
    fun `en-US negative amount places minus before symbol`() {
        val formatted = PriceFormatter.format(BigDecimal("-1234.56"), Currency.USD, Locale.US)
        assertThat(formatted).startsWith("-$")
    }

    @Test
    fun `en-US formats all current currencies`() {
        val cases = listOf(
            Currency.USD to "$1.23",
            Currency.GBP to "£1.23",
            Currency.EUR to "€1.23",
        )

        cases.forEach { (currency, expected) ->
            assertThat(PriceFormatter.format(BigDecimal("1.23"), currency, Locale.US))
                .isEqualTo(expected)
        }
    }

    @Test
    fun `en-US formats exact small price boundaries`() {
        val cases = listOf(
            "-1.00" to "-$1.000000",
            "-1.000001" to "-$1.00",
            "0" to "$0.000000",
            "1.00" to "$1.000000",
            "1.000001" to "$1.00",
        )

        cases.forEach { (amount, expected) ->
            assertThat(PriceFormatter.format(BigDecimal(amount), Currency.USD, Locale.US))
                .isEqualTo(expected)
        }
    }

    @Test
    fun `en-US formats two decimal half-even rounding`() {
        val cases = listOf(
            "1.005" to "$1.00",
            "1.015" to "$1.02",
        )

        cases.forEach { (amount, expected) ->
            assertThat(PriceFormatter.format(BigDecimal(amount), Currency.USD, Locale.US))
                .isEqualTo(expected)
        }
    }

    @Test
    fun `en-US formats six decimal half-even rounding for small prices`() {
        val cases = listOf(
            "0.1234545" to "$0.123454",
            "0.1234555" to "$0.123456",
            "0.1234565" to "$0.123456",
        )

        cases.forEach { (amount, expected) ->
            assertThat(PriceFormatter.format(BigDecimal(amount), Currency.USD, Locale.US))
                .isEqualTo(expected)
        }
    }

    @Test
    fun `en-US keeps rounded-up under-million value unshortened`() {
        assertThat(PriceFormatter.format(BigDecimal("999999.999"), Currency.USD, Locale.US))
            .isEqualTo("$1,000,000.00")
    }

    @Test
    fun `en-US formats exact abbreviation tier starts`() {
        val cases = listOf(
            "1000000" to "$1.00M",
            "1000000000" to "$1.00B",
            "1000000000000" to "$1.00T",
            "1000000000000000" to "$1.00Q",
        )

        cases.forEach { (amount, expected) ->
            assertThat(PriceFormatter.format(BigDecimal(amount), Currency.USD, Locale.US))
                .isEqualTo(expected)
        }
    }

    @Test
    fun `en-US formats abbreviation round-up thresholds`() {
        val cases = listOf(
            "999995000" to "$1.00B",
            "999995000000" to "$1.00T",
            "999995000000000" to "$1.00Q",
        )

        cases.forEach { (amount, expected) ->
            assertThat(PriceFormatter.format(BigDecimal(amount), Currency.USD, Locale.US))
                .isEqualTo(expected)
        }
    }

    @Test
    fun `en-US keeps values that round to quintillion unshortened`() {
        val cases = listOf(
            "999994999999999999.99" to "$999.99Q",
            "999995000000000000" to "$999,995,000,000,000,000.00",
            "1000000000000000000" to "$1,000,000,000,000,000,000.00",
        )

        cases.forEach { (amount, expected) ->
            assertThat(PriceFormatter.format(BigDecimal(amount), Currency.USD, Locale.US))
                .isEqualTo(expected)
        }
    }

    @Test
    fun `ar-SA normal amount uses Arabic Indic digits with currency after number`() {
        val locale = Locale.forLanguageTag("ar-SA")
        val formatted = PriceFormatter.format(BigDecimal("1234.56"), Currency.USD, locale)
        val lastDigitIndex = formatted.indexOfLast { it.isDigit() }
        val currencyIndex = currencyRepresentationIndex(formatted, Currency.USD, locale)

        assertThat(formatted.any { it in '\u0660'..'\u0669' }).isTrue()
        assertThat(lastDigitIndex).isAtLeast(0)
        assertThat(currencyIndex).isGreaterThan(lastDigitIndex)
    }

    @Test
    fun `placeholders contain one placeholder no digits and currency representation`() {
        val locales = listOf(
            Locale.US,
            Locale.UK,
            Locale.GERMANY,
            Locale.FRANCE,
            Locale.forLanguageTag("tr-TR"),
            Locale.forLanguageTag("ar-SA"),
        )
        val currencies = listOf(Currency.USD, Currency.GBP, Currency.EUR)

        locales.forEach { locale ->
            currencies.forEach { currency ->
                val formatted = PriceFormatter.formatMissing(currency, locale)

                assertContainsSinglePlaceholder(formatted)
                assertNoDigits(formatted)
                assertContainsCurrencyRepresentation(formatted, currency, locale)
            }
        }
    }

    @Test
    fun `symbol-after locales place placeholder before currency representation`() {
        val locales = listOf(Locale.GERMANY, Locale.FRANCE, Locale.forLanguageTag("ar-SA"))
        val currencies = listOf(Currency.USD, Currency.GBP, Currency.EUR)

        locales.forEach { locale ->
            currencies.forEach { currency ->
                val formatted = PriceFormatter.formatMissing(currency, locale)
                val placeholderIndex = formatted.indexOf('—')
                val currencyIndex = currencyRepresentationIndex(formatted, currency, locale)

                assertThat(placeholderIndex).isAtLeast(0)
                assertThat(currencyIndex).isGreaterThan(placeholderIndex)
            }
        }
    }

    @Test
    fun `abbreviation suffixes stay adjacent to digits across symbol layouts`() {
        val locales = listOf(
            Locale.US,
            Locale.GERMANY,
            Locale.FRANCE,
            Locale.forLanguageTag("tr-TR"),
            Locale.forLanguageTag("ar-SA"),
        )
        val cases = listOf(
            BigDecimal("49491394") to "M",
            BigDecimal("1009900243") to "B",
            BigDecimal("3084938574102") to "T",
            BigDecimal("27913084938574102") to "Q",
        )

        locales.forEach { locale ->
            cases.forEach { (amount, suffix) ->
                val formatted = PriceFormatter.format(amount, Currency.USD, locale)

                assertSuffixAdjacentToDigit(formatted, suffix)
            }
        }
    }

    @Test
    fun `en-US negative large amounts do not abbreviate`() {
        val cases = listOf(
            "-1000000" to "-$1,000,000.00",
            "-999999999.99" to "-$999,999,999.99",
        )

        cases.forEach { (amount, expected) ->
            assertThat(PriceFormatter.format(BigDecimal(amount), Currency.USD, Locale.US))
                .isEqualTo(expected)
        }
    }

    @Test
    fun `de-DE negative amount keeps minus sign and separators`() {
        val formatted = PriceFormatter.format(BigDecimal("-1234.56"), Currency.USD, Locale.GERMANY)

        assertThat(formatted).startsWith("-")
        assertThat(formatted).contains("1.234")
        assertThat(formatted).contains(",56")
    }

    private fun assertSuffixAdjacentToDigit(formatted: String, suffix: String) {
        val suffixIndex = formatted.indexOf(suffix)

        assertThat(suffixIndex).isGreaterThan(0)
        assertThat(formatted[suffixIndex - 1].isDigit()).isTrue()
        assertThat(formatted).doesNotContain("\$$suffix")
        assertThat(formatted).doesNotContain("US\$$suffix")
        assertThat(formatted).doesNotContain("\$US$suffix")
    }

    private fun assertContainsSinglePlaceholder(formatted: String) {
        assertThat(formatted.count { it == '—' }).isEqualTo(1)
    }

    private fun assertNoDigits(formatted: String) {
        assertThat(formatted.none { it.isDigit() }).isTrue()
    }

    private fun assertContainsCurrencyRepresentation(
        formatted: String,
        currency: Currency,
        locale: Locale,
    ) {
        assertThat(currencyRepresentationIndex(formatted, currency, locale)).isAtLeast(0)
    }

    private fun currencyRepresentationIndex(
        formatted: String,
        currency: Currency,
        locale: Locale,
    ): Int {
        val symbol = JavaCurrency.getInstance(currency.name).getSymbol(locale)
        val symbolIndex = formatted.indexOf(symbol)
        if (symbolIndex >= 0) return symbolIndex

        return formatted.indexOf(currency.name)
    }
}

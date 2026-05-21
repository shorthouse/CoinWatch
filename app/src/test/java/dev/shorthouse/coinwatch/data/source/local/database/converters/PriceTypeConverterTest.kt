package dev.shorthouse.coinwatch.data.source.local.database.converters

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.rule.LocaleRule
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.util.Locale

class PriceTypeConverterTest {

    @get:Rule
    val localeRule = LocaleRule(Locale.US)

    private val converter = PriceTypeConverter()

    @Test
    fun `JSON shape contains price and currency but not derived fields`() {
        val json = converter.fromPrice(Price("1.23", Currency.USD))

        assertThat(json).contains("\"price\":\"1.23\"")
        assertThat(json).contains("\"currency\":\"USD\"")
        assertThat(json).doesNotContain("formattedAmount")
        assertThat(json).doesNotContain("\"amount\"")
    }

    @Test
    fun `JSON shape remains raw model data under non-US FORMAT locale`() {
        localeRule.withLocale(Locale.GERMANY) {
            val json = converter.fromPrice(Price("1234.56", Currency.USD))

            assertThat(json).contains("\"price\":\"1234.56\"")
            assertThat(json).contains("\"currency\":\"USD\"")
            assertThat(json).doesNotContain("1.234,56")
        }
    }

    @Test
    fun `Deserialization tolerates legacy JSON with extra derived fields`() {
        val legacyJson =
            """{"price":"29490.95","currency":"USD","amount":"29490.95","formattedAmount":"$29,490.95"}"""

        val price = converter.toPrice(legacyJson)

        assertThat(price.price).isEqualTo("29490.95")
        assertThat(price.currency).isEqualTo(Currency.USD)
    }

    @Test
    fun `Deserialization ignores stale legacy derived fields`() {
        val legacyJson = """{"price":"1.23","currency":"USD","amount":"999","formattedAmount":"$999.00"}"""

        val price = converter.toPrice(legacyJson)

        assertThat(price.price).isEqualTo("1.23")
        assertThat(price.currency).isEqualTo(Currency.USD)
        assertThat(price.amount).isEqualTo(BigDecimal("1.23"))
        assertThat(price.formattedAmount).isEqualTo("$1.23")
    }

    @Test
    fun `Round trip preserves price and currency for all current currencies`() {
        val currencies = listOf(Currency.USD, Currency.GBP, Currency.EUR)

        currencies.forEach { currency ->
            val original = Price("1.23", currency)

            val restored = converter.toPrice(converter.fromPrice(original))

            assertThat(restored.price).isEqualTo(original.price)
            assertThat(restored.currency).isEqualTo(original.currency)
        }
    }

    @Test
    fun `Computed amount works after deserialization`() {
        val restored = converter.toPrice(converter.fromPrice(Price("1.23", Currency.USD)))

        assertThat(restored.amount).isEqualTo(BigDecimal("1.23"))
    }

    @Test
    fun `Computed formattedAmount works after deserialization under en-US`() {
        val restored = converter.toPrice(converter.fromPrice(Price("1.23", Currency.USD)))

        assertThat(restored.formattedAmount).isEqualTo("$1.23")
    }

    @Test
    fun `Null price round trips and renders placeholder`() {
        val restored = converter.toPrice(converter.fromPrice(Price(null, Currency.USD)))

        assertThat(restored.price).isNull()
        assertThat(restored.amount).isEqualTo(BigDecimal.ZERO)
        assertThat(restored.formattedAmount).isEqualTo("$—")
    }
}

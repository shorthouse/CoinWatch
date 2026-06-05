package dev.shorthouse.coinwatch.model

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.rule.LocaleRule
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.util.Locale

class GlobalMarketTest {

    @get:Rule
    val localeRule = LocaleRule(Locale.US)

    private fun globalMarket(
        btcDominancePercentage: BigDecimal = BigDecimal("54.2"),
        coinsUp24h: Int = 2841,
        coinsDown24h: Int = 1893,
    ) = GlobalMarket(
        totalMarketCap = Price("2410000000000", Currency.USD),
        volume24h = Price("98200000000", Currency.USD),
        btcDominancePercentage = btcDominancePercentage,
        coinsUp24h = coinsUp24h,
        coinsDown24h = coinsDown24h,
    )

    @Test
    fun `When btc dominance within range should map to fraction`() {
        val globalMarket = globalMarket(btcDominancePercentage = BigDecimal("54.2"))

        assertThat(globalMarket.btcDominanceFraction).isWithin(0.0001f).of(0.542f)
    }

    @Test
    fun `When btc dominance below zero should clamp fraction to zero`() {
        val globalMarket = globalMarket(btcDominancePercentage = BigDecimal("-10"))

        assertThat(globalMarket.btcDominanceFraction).isEqualTo(0f)
    }

    @Test
    fun `When btc dominance above one hundred should clamp fraction to one`() {
        val globalMarket = globalMarket(btcDominancePercentage = BigDecimal("150"))

        assertThat(globalMarket.btcDominanceFraction).isEqualTo(1f)
    }

    @Test
    fun `When formatting btc dominance should use two decimal places`() {
        val globalMarket = globalMarket(btcDominancePercentage = BigDecimal("54.2"))

        assertThat(globalMarket.formattedBtcDominance).isEqualTo("54.20%")
    }

    @Test
    fun `When format locale is German should format btc dominance as localized percentage`() {
        localeRule.withLocale(Locale.GERMANY) {
            val globalMarket = globalMarket(btcDominancePercentage = BigDecimal("54.2"))

            assertThat(globalMarket.formattedBtcDominance).contains("54,20")
        }
    }

    @Test
    fun `When formatting coin counts should group thousands`() {
        val globalMarket = globalMarket(coinsUp24h = 2841, coinsDown24h = 1893)

        assertThat(globalMarket.formattedCoinsUp24h).isEqualTo("2,841")
        assertThat(globalMarket.formattedCoinsDown24h).isEqualTo("1,893")
    }

    @Test
    fun `When coins up and down present should weight by counts`() {
        val globalMarket = globalMarket(coinsUp24h = 2841, coinsDown24h = 1893)

        assertThat(globalMarket.coinsUpWeight).isEqualTo(2841f)
        assertThat(globalMarket.coinsDownWeight).isEqualTo(1893f)
    }

    @Test
    fun `When no coins moved should split weights equally`() {
        val globalMarket = globalMarket(coinsUp24h = 0, coinsDown24h = 0)

        assertThat(globalMarket.coinsUpWeight).isEqualTo(1f)
        assertThat(globalMarket.coinsDownWeight).isEqualTo(1f)
    }
}

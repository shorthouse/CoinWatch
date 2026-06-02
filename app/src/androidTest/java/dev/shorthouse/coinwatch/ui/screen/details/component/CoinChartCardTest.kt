package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.PriceEntry
import dev.shorthouse.coinwatch.rule.LocaleRule
import dev.shorthouse.coinwatch.ui.assertion.assertCurrencyAfterAmount
import dev.shorthouse.coinwatch.ui.assertion.assertNoTextContaining
import dev.shorthouse.coinwatch.ui.assertion.assertTextContaining
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.util.Locale

class CoinChartCardTest {

    @get:Rule(order = 0)
    val localeRule = LocaleRule()

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun when_scrubbingChart_should_formatScrubbedPriceWithCoinChartCurrency() {
        composeTestRule.setContent {
            AppTheme {
                CoinChartCard(
                    currentPrice = Price("1000.00", currency = Currency.USD),
                    coinChart = CoinChart(
                        currency = Currency.USD,
                        priceHistory = persistentListOf(
                            PriceEntry(BigDecimal("100.00"), 1L, "01 Jan"),
                            PriceEntry(BigDecimal("110.00"), 2L, "02 Jan"),
                            PriceEntry(BigDecimal("120.00"), 3L, "03 Jan")
                        ),
                        periodPriceChangePercentage = Percentage("20.00")
                    ),
                    chartPeriod = ChartPeriod.Day,
                    onClickChartPeriod = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
        }

        composeTestRule.onNodeWithText("$1,000.00").assertExists()

        composeTestRule.onNodeWithTag("scrubPriceGraphCanvas")
            .performTouchInput {
                down(Offset(x = 1f, y = height / 2f))
            }

        composeTestRule.onNodeWithText("$100.00").assertExists()

        composeTestRule.onNodeWithTag("scrubPriceGraphCanvas")
            .performTouchInput {
                moveTo(Offset(x = width / 2f, y = height / 2f))
            }

        composeTestRule.onNodeWithText("$110.00").assertExists()

        composeTestRule.onNodeWithTag("scrubPriceGraphCanvas")
            .performTouchInput {
                moveTo(Offset(x = width - 1f, y = height / 2f))
            }

        composeTestRule.onNodeWithText("$120.00").assertExists()

        composeTestRule.onNodeWithTag("scrubPriceGraphCanvas")
            .performTouchInput { up() }
    }

    @Test
    fun when_scrubbingGbpChart_should_formatScrubbedPriceWithCoinChartCurrency() {
        composeTestRule.setContent {
            AppTheme {
                CoinChartCard(
                    currentPrice = Price("1000.00", currency = Currency.GBP),
                    coinChart = CoinChart(
                        currency = Currency.GBP,
                        priceHistory = persistentListOf(
                            PriceEntry(BigDecimal("100.00"), 1L, "01 Jan"),
                            PriceEntry(BigDecimal("110.00"), 2L, "02 Jan"),
                            PriceEntry(BigDecimal("120.00"), 3L, "03 Jan")
                        ),
                        periodPriceChangePercentage = Percentage("20.00")
                    ),
                    chartPeriod = ChartPeriod.Day,
                    onClickChartPeriod = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
        }

        composeTestRule.onNodeWithText("£1,000.00").assertExists()

        composeTestRule.onNodeWithTag("scrubPriceGraphCanvas")
            .performTouchInput {
                down(Offset(x = 1f, y = height / 2f))
            }

        composeTestRule.onNodeWithText("£100.00").assertExists()

        composeTestRule.onNodeWithTag("scrubPriceGraphCanvas")
            .performTouchInput {
                moveTo(Offset(x = width / 2f, y = height / 2f))
            }

        composeTestRule.onNodeWithText("£110.00").assertExists()

        composeTestRule.onNodeWithTag("scrubPriceGraphCanvas")
            .performTouchInput {
                moveTo(Offset(x = width - 1f, y = height / 2f))
            }

        composeTestRule.onNodeWithText("£120.00").assertExists()

        composeTestRule.onNodeWithTag("scrubPriceGraphCanvas")
            .performTouchInput { up() }
    }

    @Test
    fun when_formatLocaleIsGerman_should_formatCurrentAndScrubbedPricesWithGermanFormatting() {
        localeRule.withLocale(Locale.GERMANY) {
            composeTestRule.setContent {
                AppTheme {
                    CoinChartCard(
                        currentPrice = Price("1000.00", currency = Currency.USD),
                        coinChart = CoinChart(
                            currency = Currency.USD,
                            priceHistory = persistentListOf(
                                PriceEntry(BigDecimal("100.00"), 1L, "01 Jan"),
                                PriceEntry(BigDecimal("110.00"), 2L, "02 Jan"),
                                PriceEntry(BigDecimal("120.00"), 3L, "03 Jan")
                            ),
                            periodPriceChangePercentage = Percentage("20.00")
                        ),
                        chartPeriod = ChartPeriod.Day,
                        onClickChartPeriod = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
            }

            composeTestRule.apply {
                assertTextContaining("1.000,00")
                assertCurrencyAfterAmount("1.000,00")
                assertNoTextContaining("1,000.00")

                onNodeWithTag("scrubPriceGraphCanvas")
                    .performTouchInput {
                        down(Offset(x = 1f, y = height / 2f))
                    }

                assertTextContaining("100,00")
                assertCurrencyAfterAmount("100,00")
                assertNoTextContaining("100.00")

                onNodeWithTag("scrubPriceGraphCanvas")
                    .performTouchInput { up() }
            }
        }
    }
}

package dev.shorthouse.coinwatch.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.test.core.app.ActivityScenario
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.shorthouse.coinwatch.MainActivity
import dev.shorthouse.coinwatch.e2e.fake.FakeCoinNetworkDataSource
import dev.shorthouse.coinwatch.e2e.fixture.Bitcoin
import dev.shorthouse.coinwatch.e2e.fixture.Ethereum
import dev.shorthouse.coinwatch.e2e.fixture.respondWithCoins
import dev.shorthouse.coinwatch.e2e.support.awaitText
import dev.shorthouse.coinwatch.e2e.support.launchMainActivityAfterPreLaunchSetup
import dev.shorthouse.coinwatch.model.Percentage
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Locale
import javax.inject.Inject

@HiltAndroidTest
class PercentageLocaleE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createEmptyComposeRule()

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var originalDefaultLocale: Locale
    private lateinit var originalDisplayLocale: Locale
    private lateinit var originalFormatLocale: Locale

    @Inject
    lateinit var fakeCoinNetworkDataSource: FakeCoinNetworkDataSource

    @Before
    fun setup() {
        originalDefaultLocale = Locale.getDefault()
        originalDisplayLocale = Locale.getDefault(Locale.Category.DISPLAY)
        originalFormatLocale = Locale.getDefault(Locale.Category.FORMAT)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        if (this::activityScenario.isInitialized) {
            activityScenario.onActivity {
                restoreOriginalLocales()
            }
            activityScenario.close()
        }
        restoreOriginalLocales()
    }

    @Test
    fun when_formatLocaleIsGerman_should_displayMarketPercentageWithGermanFormatting() {
        val refreshedBitcoinPercentage = "3.21"
        val refreshedBitcoin = Bitcoin.coinApiModel().copy(
            priceChangePercentage24h = refreshedBitcoinPercentage,
        )
        val usFormattedPercentage = withFormatLocale(Locale.US) {
            Percentage(refreshedBitcoinPercentage).formattedAmount
        }
        val germanFormattedPercentage = withFormatLocale(Locale.GERMANY) {
            Percentage(refreshedBitcoinPercentage).formattedAmount
        }

        activityScenario = launchMainActivityAfterPreLaunchSetup()

        composeTestRule.apply {
            awaitText(Bitcoin.NAME)

            activityScenario.onActivity {
                setTestLocales(formatLocale = Locale.GERMANY)
            }
            fakeCoinNetworkDataSource.respondWithCoins(
                refreshedBitcoin,
                Ethereum.coinApiModel(),
            )

            onNodeWithText(Bitcoin.NAME)
                .onParent()
                .performTouchInput {
                    swipeDown()
                }

            awaitText(germanFormattedPercentage)
            onNodeWithText(germanFormattedPercentage).assertIsDisplayed()
            assertThat(onAllNodesWithText(usFormattedPercentage).fetchSemanticsNodes())
                .isEmpty()
        }
    }

    private fun <T> withFormatLocale(locale: Locale, block: () -> T): T {
        val previous = Locale.getDefault(Locale.Category.FORMAT)
        Locale.setDefault(Locale.Category.FORMAT, locale)
        try {
            return block()
        } finally {
            Locale.setDefault(Locale.Category.FORMAT, previous)
        }
    }

    private fun setTestLocales(formatLocale: Locale) {
        Locale.setDefault(Locale.US)
        Locale.setDefault(Locale.Category.DISPLAY, Locale.US)
        Locale.setDefault(Locale.Category.FORMAT, formatLocale)
    }

    private fun restoreOriginalLocales() {
        Locale.setDefault(originalDefaultLocale)
        Locale.setDefault(Locale.Category.DISPLAY, originalDisplayLocale)
        Locale.setDefault(Locale.Category.FORMAT, originalFormatLocale)
    }
}

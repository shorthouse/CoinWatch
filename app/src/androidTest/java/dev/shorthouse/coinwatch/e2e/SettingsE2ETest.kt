package dev.shorthouse.coinwatch.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ActivityScenario
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.shorthouse.coinwatch.MainActivity
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.e2e.fixture.Bitcoin
import dev.shorthouse.coinwatch.e2e.fixture.Ethereum
import dev.shorthouse.coinwatch.e2e.support.awaitSelectedText
import dev.shorthouse.coinwatch.e2e.support.awaitText
import dev.shorthouse.coinwatch.e2e.support.awaitTextGone
import dev.shorthouse.coinwatch.e2e.support.launchMainActivityAfterPreLaunchSetup
import dev.shorthouse.coinwatch.model.Price
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SettingsE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createEmptyComposeRule()

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        hiltRule.inject()
        launchActivity()
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }

    @Test
    fun when_settingsOpenedFromMarket_should_displayContentAndReturnToMarket() {
        composeTestRule.apply {
            openSettingsFromMarket()

            onNodeWithText("Settings").assertIsDisplayed()
            onNodeWithText("Preferences").assertIsDisplayed()
            onNodeWithText("Currency").assertIsDisplayed()
            onNodeWithText("USD").assertIsDisplayed()
            onNodeWithText("Start screen").assertIsDisplayed()
            onNodeWithText("Market").assertIsDisplayed()
            assertTextVisibleAfterScroll("About")
            assertTextVisibleAfterScroll("CoinWatch version")
            assertTextVisibleAfterScroll("Feedback")

            onNodeWithContentDescription("Back").performClick()

            awaitText(Bitcoin.NAME)
            onNodeWithText(Ethereum.NAME).assertIsDisplayed()
        }
    }

    @Test
    fun when_currencyChanged_should_updateMarketPricesAndPersistAcrossActivityRecreate() {
        val gbpBitcoinPrice = Price(Bitcoin.RAW_PRICE, currency = Currency.GBP).formattedAmount

        composeTestRule.apply {
            openSettingsFromMarket()
            selectCurrency("GBP")
            onNodeWithContentDescription("Back").performClick()

            awaitText(gbpBitcoinPrice)

            relaunchActivity()

            awaitText(gbpBitcoinPrice)
            onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
        }
    }

    @Test
    fun when_currencyChanged_should_applyToDetailsOpenedAfterwards() {
        val eurBitcoinPrice = Price(Bitcoin.RAW_PRICE, currency = Currency.EUR).formattedAmount

        composeTestRule.apply {
            openSettingsFromMarket()
            selectCurrency("EUR")
            onNodeWithContentDescription("Back").performClick()

            awaitText(eurBitcoinPrice)
            onNodeWithText(Bitcoin.NAME).performClick()

            awaitText("Past day")
            onNodeWithText(eurBitcoinPrice).assertIsDisplayed()
        }
    }

    @Test
    fun when_startScreenChangedToSearch_should_startOnSearchAfterActivityRecreate() {
        composeTestRule.apply {
            openSettingsFromMarket()
            selectStartScreen("Search")
            awaitText("Search")

            relaunchActivity()

            awaitText("Explore coins")
            onNodeWithText("Search by name or symbol").assertIsDisplayed()
        }
    }

    @Test
    fun when_startScreenChangedToFavourites_should_startOnFavouritesAfterActivityRecreate() {
        composeTestRule.apply {
            openSettingsFromMarket()
            selectStartScreen("Favourites")
            awaitText("Favourites")

            relaunchActivity()

            awaitText("No favourite coins")
        }
    }

    @Test
    fun when_startScreenChangedToPulse_should_startOnPulseAfterActivityRecreate() {
        composeTestRule.apply {
            openSettingsFromMarket()
            selectStartScreen("Pulse")
            awaitText("Pulse")

            relaunchActivity()

            awaitText("Market Mood")
            onNodeWithText("Fear").assertIsDisplayed()
        }
    }

    private fun ComposeTestRule.openSettingsFromMarket() {
        awaitText(Bitcoin.NAME)
        onNodeWithContentDescription("More").performClick()
        onNodeWithText("Settings").performClick()
        awaitText("Preferences")
    }

    private fun ComposeTestRule.selectCurrency(currencyLabel: String) {
        onNodeWithText("Currency").performClick()
        awaitText("Coin currency")
        onNodeWithText(currencyLabel).performClick()
        awaitTextGone("Coin currency")
    }

    private fun ComposeTestRule.selectStartScreen(startScreenLabel: String) {
        onNodeWithText("Start screen").performClick()
        awaitText("App start screen")
        onNodeWithText(startScreenLabel).performClick()
        awaitTextGone("App start screen")
    }

    private fun launchActivity() {
        activityScenario = launchMainActivityAfterPreLaunchSetup()
    }

    private fun relaunchActivity() {
        activityScenario.close()
        launchActivity()
    }

    private fun ComposeTestRule.assertTextVisibleAfterScroll(text: String) {
        onNodeWithText(text)
            .performScrollTo()
            .assertIsDisplayed()
    }
}

package dev.shorthouse.coinwatch.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.shorthouse.coinwatch.MainActivity
import dev.shorthouse.coinwatch.data.source.remote.FakeCoinNetworkDataSource
import dev.shorthouse.coinwatch.fixture.Bitcoin
import dev.shorthouse.coinwatch.fixture.Ethereum
import dev.shorthouse.coinwatch.fixture.coinChartApiModel
import dev.shorthouse.coinwatch.fixture.failCoinChart
import dev.shorthouse.coinwatch.fixture.failCoinDetails
import dev.shorthouse.coinwatch.fixture.respondWithCoinChart
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DetailsE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var fakeCoinNetworkDataSource: FakeCoinNetworkDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun when_detailsOpenedFromMarket_should_displayPrimaryContent() {
        composeTestRule.apply {
            openBitcoinDetailsFromMarket()

            assertBitcoinDetailsHeader()
            onNodeWithText("+1.77%").assertIsDisplayed()
            onNodeWithText("Past day").assertIsDisplayed()
            assertTextVisibleAfterScroll("About")
            assertTextVisibleAfterScroll("Market Stats")
            assertTextVisibleAfterScroll("Supply")
            assertTextVisibleAfterScroll("Links")
            assertTextVisibleAfterScroll("Website")
        }
    }

    @Test
    fun when_detailsBackClicked_should_returnToMarket() {
        composeTestRule.apply {
            openBitcoinDetailsFromMarket()

            onNodeWithContentDescription("Back").performClick()

            awaitText("Market")
            onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
            onNodeWithText(Ethereum.NAME).assertIsDisplayed()
        }
    }

    @Test
    fun when_chartPeriodChanged_should_refetchChartAndUpdatePeriod() {
        fakeCoinNetworkDataSource.respondWithCoinChart { chartPeriod ->
            if (chartPeriod == ChartPeriod.Week.stringName) {
                coinChartApiModel(priceChangePercentage = "4.20")
            } else {
                coinChartApiModel()
            }
        }

        composeTestRule.apply {
            openBitcoinDetailsFromMarket()
            onNodeWithText("+1.77%").assertIsDisplayed()

            onNodeWithText("1W").performClick()

            awaitText("Past week")
            awaitText("+4.20%")
        }
    }

    @Test
    fun when_coinDetailsLoadFails_should_showErrorState() {
        fakeCoinNetworkDataSource.failCoinDetails()

        composeTestRule.apply {
            awaitText(Bitcoin.NAME)
            onNodeWithText(Bitcoin.NAME).performClick()

            assertDetailsError("Unable to fetch coin details")
        }
    }

    @Test
    fun when_coinChartLoadFails_should_showErrorState() {
        fakeCoinNetworkDataSource.failCoinChart()

        composeTestRule.apply {
            awaitText(Bitcoin.NAME)
            onNodeWithText(Bitcoin.NAME).performClick()

            assertDetailsError("Unable to fetch coin chart")
        }
    }

    private fun ComposeTestRule.openBitcoinDetailsFromMarket() {
        awaitText(Bitcoin.NAME)
        onNodeWithText(Bitcoin.NAME).performClick()
        awaitText("Past day")
    }

    private fun ComposeTestRule.assertBitcoinDetailsHeader() {
        onNodeWithContentDescription("Back").assertIsDisplayed()
        onNodeWithContentDescription("Favourite").assertIsDisplayed()
        onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
        onNodeWithText(Bitcoin.SYMBOL).assertIsDisplayed()
        onNodeWithText(Bitcoin.FORMATTED_PRICE).assertIsDisplayed()
    }

    private fun ComposeTestRule.assertDetailsError(message: String) {
        awaitText("An error has occurred")
        onNodeWithContentDescription("Back").assertIsDisplayed()
        onNodeWithText(message).assertIsDisplayed()
    }

    private fun ComposeTestRule.assertTextVisibleAfterScroll(text: String) {
        onNodeWithText(text)
            .performScrollTo()
            .assertIsDisplayed()
    }
}

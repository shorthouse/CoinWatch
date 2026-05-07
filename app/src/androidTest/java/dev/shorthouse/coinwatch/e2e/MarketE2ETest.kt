package dev.shorthouse.coinwatch.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.shorthouse.coinwatch.MainActivity
import dev.shorthouse.coinwatch.data.source.remote.FakeCoinNetworkDataSource
import dev.shorthouse.coinwatch.fixture.Bitcoin
import dev.shorthouse.coinwatch.fixture.Ethereum
import dev.shorthouse.coinwatch.model.Price
import javax.inject.Inject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MarketE2ETest {

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
    fun when_bitcoinClicked_should_displayBitcoinDetails() {
        composeTestRule.awaitText(Bitcoin.NAME)
        composeTestRule.onNodeWithText(Bitcoin.NAME).performClick()

        composeTestRule.awaitText("Past day")
        composeTestRule.awaitText(Bitcoin.FORMATTED_PRICE)

        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Favourite").assertIsDisplayed()
        composeTestRule.onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
        composeTestRule.onNodeWithText(Bitcoin.SYMBOL).assertIsDisplayed()
        composeTestRule.onNodeWithText(Bitcoin.FORMATTED_PRICE).assertIsDisplayed()
        composeTestRule.onNodeWithText("Past day").assertIsDisplayed()
    }

    @Test
    fun when_pullToRefreshUpdatesCachedMarketCoins() {
        val refreshedBitcoinPrice = "31415.92"
        val refreshedBitcoin = Bitcoin.coinApiModel().copy(
            currentPrice = refreshedBitcoinPrice,
        )
        val refreshedBitcoinFormattedPrice = Price(refreshedBitcoinPrice).formattedAmount

        composeTestRule.awaitText(Bitcoin.NAME)
        composeTestRule.awaitText(Bitcoin.FORMATTED_PRICE)

        fakeCoinNetworkDataSource.respondWithCoins(
            refreshedBitcoin,
            Ethereum.coinApiModel(),
        )

        composeTestRule.onNodeWithText(Bitcoin.NAME)
            .onParent()
            .performTouchInput {
                swipeDown()
            }

        composeTestRule.awaitText(refreshedBitcoinFormattedPrice)
        composeTestRule.onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
        composeTestRule.onNodeWithText(refreshedBitcoinFormattedPrice).assertIsDisplayed()
    }

    @Test
    fun when_marketRefreshFails_should_preserveCachedCoinsAndShowNetworkError() {
        composeTestRule.awaitText(Bitcoin.NAME)
        composeTestRule.awaitText(Bitcoin.FORMATTED_PRICE)

        fakeCoinNetworkDataSource.failCoins()

        composeTestRule.onNodeWithText(Bitcoin.NAME)
            .onParent()
            .performTouchInput {
                swipeDown()
            }

        composeTestRule.awaitText("Latest coin data unavailable")
        composeTestRule.onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
        composeTestRule.onNodeWithText(Bitcoin.FORMATTED_PRICE).assertIsDisplayed()
    }

    @Test
    fun when_marketSortChanged_should_preservePreferenceAcrossNavigation() {
        composeTestRule.awaitText(Bitcoin.NAME)

        composeTestRule.onNodeWithText("Popular").performClick()
        composeTestRule.awaitSelectedText("Popular")

        composeTestRule.onNodeWithText("Favourites").performClick()
        composeTestRule.awaitText("No favourite coins")

        composeTestRule.onNodeWithText("Market").performClick()
        composeTestRule.awaitText(Bitcoin.NAME)

        composeTestRule.awaitSelectedText("Popular")
        composeTestRule.onNodeWithText("Popular").assertIsSelected()
    }
}

package dev.shorthouse.coinwatch.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
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
import dev.shorthouse.coinwatch.e2e.fake.FakeCoinNetworkDataSource
import dev.shorthouse.coinwatch.e2e.fixture.Bitcoin
import dev.shorthouse.coinwatch.e2e.fixture.Ethereum
import dev.shorthouse.coinwatch.e2e.fixture.failCoins
import dev.shorthouse.coinwatch.e2e.fixture.respondWithCoins
import dev.shorthouse.coinwatch.model.Price
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

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
    fun when_marketCoinClicked_should_displayCoinDetails() {
        composeTestRule.apply {
            onNodeWithText(Bitcoin.NAME).performClick()

            onNodeWithContentDescription("Back").assertIsDisplayed()
            onNodeWithContentDescription("Favourite").assertIsDisplayed()
            onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
            onNodeWithText(Bitcoin.SYMBOL).assertIsDisplayed()
            onNodeWithText(Bitcoin.FORMATTED_PRICE).assertIsDisplayed()
            onNodeWithText("Past day").assertIsDisplayed()
        }
    }

    @Test
    fun when_marketPulledToRefresh_should_updateCachedCoins() {
        val refreshedBitcoinPrice = "31415.92"
        val refreshedBitcoin = Bitcoin.coinApiModel().copy(
            currentPrice = refreshedBitcoinPrice,
        )
        val refreshedBitcoinFormattedPrice = Price(refreshedBitcoinPrice).formattedAmount

        composeTestRule.awaitText(Bitcoin.NAME)

        fakeCoinNetworkDataSource.respondWithCoins(
            refreshedBitcoin,
            Ethereum.coinApiModel(),
        )

        composeTestRule.apply {
            onNodeWithText(Bitcoin.NAME)
                .onParent()
                .performTouchInput {
                    swipeDown()
                }

            awaitText(refreshedBitcoinFormattedPrice)
            onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
            onNodeWithText(refreshedBitcoinFormattedPrice).assertIsDisplayed()
        }
    }

    @Test
    fun when_marketRefreshFails_should_keepCachedCoinsAndShowError() {
        composeTestRule.apply {
            awaitText(Bitcoin.NAME)

            fakeCoinNetworkDataSource.failCoins()

            onNodeWithText(Bitcoin.NAME)
                .onParent()
                .performTouchInput {
                    swipeDown()
                }

            awaitText("Latest coin data unavailable")
            onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
            onNodeWithText(Bitcoin.FORMATTED_PRICE).assertIsDisplayed()
        }
    }

    @Test
    fun when_marketSortChanged_should_preserveSelectionAcrossBottomNavigation() {
        composeTestRule.apply {
            onNodeWithText("Popular").assertIsNotSelected()

            onNodeWithText("Popular").performClick()

            onNodeWithText("Favourites").performClick()

            onNodeWithText("Market").performClick()

            onNodeWithText("Popular").assertIsSelected()
        }
    }
}

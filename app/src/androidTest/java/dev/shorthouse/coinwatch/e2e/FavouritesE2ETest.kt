package dev.shorthouse.coinwatch.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.shorthouse.coinwatch.MainActivity
import dev.shorthouse.coinwatch.e2e.fake.FakeCoinNetworkDataSource
import dev.shorthouse.coinwatch.e2e.fixture.Bitcoin
import dev.shorthouse.coinwatch.e2e.fixture.bitcoinFavouriteCoinApiModel
import dev.shorthouse.coinwatch.e2e.fixture.failFavouriteCoins
import dev.shorthouse.coinwatch.e2e.fixture.respondWithFavouriteCoins
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class FavouritesE2ETest {

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
    fun when_favouritesOpenedWithNoFavourites_should_showEmptyState() {
        composeTestRule.apply {
            onNodeWithText("Favourites").performClick()

            awaitText("No favourite coins")
            onNodeWithText("Tap the").assertIsDisplayed()
            onNodeWithContentDescription("Favourite").assertIsDisplayed()
            onNodeWithText("to favourite a coin").assertIsDisplayed()
        }
    }

    @Test
    fun when_coinFavouritedFromDetails_should_appearInFavourites() {
        fakeCoinNetworkDataSource.respondWithFavouriteCoins(bitcoinFavouriteCoinApiModel())

        composeTestRule.apply {
            favouriteBitcoinFromMarketDetails()
            openFavourites()

            awaitText(Bitcoin.NAME)
            onNodeWithText(Bitcoin.SYMBOL).assertIsDisplayed()
            onNodeWithText(Bitcoin.FORMATTED_PRICE).assertIsDisplayed()
        }
    }

    @Test
    fun when_favouriteCoinUnfavourited_should_showEmptyState() {
        fakeCoinNetworkDataSource.respondWithFavouriteCoins(bitcoinFavouriteCoinApiModel())

        composeTestRule.apply {
            favouriteBitcoinFromMarketDetails()
            openFavourites()
            awaitText(Bitcoin.NAME)

            onNodeWithText(Bitcoin.NAME).performClick()
            awaitText("Past day")
            onNodeWithContentDescription("Favourite").performClick()
            onNodeWithContentDescription("Back").performClick()

            awaitText("No favourite coins")
            onNodeWithText("Tap the").assertIsDisplayed()
            onNodeWithText("to favourite a coin").assertIsDisplayed()
        }
    }

    @Test
    fun when_favouritesLayoutCondensed_should_preserveSelectionAcrossBottomNavigation() {
        fakeCoinNetworkDataSource.respondWithFavouriteCoins(bitcoinFavouriteCoinApiModel())

        composeTestRule.apply {
            favouriteBitcoinFromMarketDetails()
            openFavourites()
            awaitText(Bitcoin.NAME)

            onNodeWithContentDescription("Condense favourites list").performClick()
            awaitContentDescription("Expand favourites list")

            onNodeWithText("Market").performClick()
            onNodeWithText("Favourites").performClick()

            onNodeWithContentDescription("Expand favourites list").assertIsDisplayed()
        }
    }

    @Test
    fun when_favouritesSortChanged_should_preserveSelectionAcrossBottomNavigation() {
        fakeCoinNetworkDataSource.respondWithFavouriteCoins(bitcoinFavouriteCoinApiModel())

        composeTestRule.apply {
            favouriteBitcoinFromMarketDetails()
            openFavourites()
            awaitText(Bitcoin.NAME)

            onNodeWithText("Popular").assertIsNotSelected()
            onNodeWithText("Popular").performClick()
            awaitSelectedText("Popular")

            onNodeWithText("Market").performClick()
            onNodeWithText("Favourites").performClick()

            onNodeWithText("Popular").assertIsSelected()
        }
    }

    @Test
    fun when_favouritesRefreshFails_should_keepCachedFavouritesAndShowError() {
        fakeCoinNetworkDataSource.respondWithFavouriteCoins(bitcoinFavouriteCoinApiModel())

        composeTestRule.apply {
            favouriteBitcoinFromMarketDetails()
            openFavourites()
            awaitText(Bitcoin.NAME)

            fakeCoinNetworkDataSource.failFavouriteCoins()

            onNodeWithText(Bitcoin.NAME)
                .performTouchInput {
                    swipeDown()
                }

            awaitText("Latest favourite coin data unavailable")
            onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
            onNodeWithText(Bitcoin.FORMATTED_PRICE).assertIsDisplayed()
        }
    }

    private fun ComposeTestRule.favouriteBitcoinFromMarketDetails() {
        awaitText(Bitcoin.NAME)
        onNodeWithText(Bitcoin.NAME).performClick()
        awaitText("Past day")
        onNodeWithContentDescription("Favourite").performClick()
        onNodeWithContentDescription("Back").performClick()
    }

    private fun ComposeTestRule.openFavourites() {
        onNodeWithText("Favourites").performClick()
    }
}

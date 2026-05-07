package dev.shorthouse.coinwatch.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.shorthouse.coinwatch.MainActivity
import dev.shorthouse.coinwatch.data.source.remote.FakeCoinNetworkDataSource
import dev.shorthouse.coinwatch.fixture.Bitcoin
import dev.shorthouse.coinwatch.fixture.Ethereum
import dev.shorthouse.coinwatch.fixture.bitcoinFavouriteCoinApiModel
import dev.shorthouse.coinwatch.fixture.bitcoinSearchResult
import dev.shorthouse.coinwatch.fixture.respondWithFavouriteCoins
import dev.shorthouse.coinwatch.fixture.respondWithSearchResults
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class NavigationE2ETest {

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
    fun when_bottomNavigationItemsClicked_should_displayExpectedScreens() {
        composeTestRule.apply {
            awaitText(Bitcoin.NAME)
            onNodeWithText(Ethereum.NAME).assertIsDisplayed()

            onNodeWithText("Favourites").performClick()
            awaitText("No favourite coins")

            onNodeWithText("Search").performClick()
            awaitText("Explore coins")
            onNodeWithText("Search by name or symbol").assertIsDisplayed()

            onNodeWithText("Market").performClick()
            awaitText(Bitcoin.NAME)
            onNodeWithText(Ethereum.NAME).assertIsDisplayed()
        }
    }

    @Test
    fun when_settingsOpenedFromMarketOverflow_should_displaySettingsAndReturnToMarket() {
        composeTestRule.apply {
            openSettingsFromMarket()

            onNodeWithText("Preferences").assertIsDisplayed()
            onNodeWithText("Currency").assertIsDisplayed()
            onNodeWithText("Start screen").assertIsDisplayed()

            onNodeWithContentDescription("Back").performClick()

            awaitText(Bitcoin.NAME)
            onNodeWithText(Ethereum.NAME).assertIsDisplayed()
        }
    }

    @Test
    fun when_marketCoinOpenedAndSystemBackPressed_should_returnToMarket() {
        composeTestRule.apply {
            openBitcoinDetailsFromMarket()

            pressSystemBack()

            awaitText(Bitcoin.NAME)
            onNodeWithText(Ethereum.NAME).assertIsDisplayed()
        }
    }

    @Test
    fun when_systemBackPressedFromSearchDetails_should_returnToSearchWithState() {
        fakeCoinNetworkDataSource.respondWithSearchResults(bitcoinSearchResult())

        composeTestRule.apply {
            openSearch()
            enterSearchQuery("Bit")
            awaitText(Bitcoin.NAME)

            onNodeWithText(Bitcoin.NAME).performClick()
            awaitText("Past day")

            pressSystemBack()

            awaitText("Bit")
            onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
            onNodeWithText(Bitcoin.SYMBOL).assertIsDisplayed()
        }
    }

    @Test
    fun when_detailsOpenedFromFavourites_should_backReturnToFavouritesWithState() {
        fakeCoinNetworkDataSource.respondWithFavouriteCoins(bitcoinFavouriteCoinApiModel())

        composeTestRule.apply {
            favouriteBitcoinFromMarketDetails()
            openFavourites()
            awaitText(Bitcoin.NAME)

            onNodeWithText(Bitcoin.NAME).performClick()
            awaitText("Past day")
            onNodeWithContentDescription("Back").performClick()

            awaitText(Bitcoin.NAME)
            onNodeWithText(Bitcoin.SYMBOL).assertIsDisplayed()
            onNodeWithText(Bitcoin.FORMATTED_PRICE).assertIsDisplayed()
        }
    }

    @Test
    fun when_searchStateSavedAndOtherDetailOpened_should_preserveSearchState() {
        fakeCoinNetworkDataSource.respondWithSearchResults(bitcoinSearchResult())

        composeTestRule.apply {
            openSearch()
            enterSearchQuery("Bit")
            awaitText(Bitcoin.NAME)

            onNodeWithText("Market").performClick()
            openBitcoinDetailsFromMarket()
            onNodeWithContentDescription("Back").performClick()
            onNodeWithText("Search").performClick()

            awaitText("Bit")
            onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
            onNodeWithText(Bitcoin.SYMBOL).assertIsDisplayed()
        }
    }

    private fun ComposeTestRule.openSearch() {
        onNodeWithText("Search").performClick()
    }

    private fun pressSystemBack() {
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun ComposeTestRule.enterSearchQuery(query: String) {
        onNodeWithText("Search coins").performClick()
        onNodeWithText("Search coins").performTextInput(query)
    }

    private fun ComposeTestRule.openSettingsFromMarket() {
        awaitText(Bitcoin.NAME)
        onNodeWithContentDescription("More").performClick()
        onNodeWithText("Settings").performClick()
        awaitText("Preferences")
    }

    private fun ComposeTestRule.openBitcoinDetailsFromMarket() {
        awaitText(Bitcoin.NAME)
        onNodeWithText(Bitcoin.NAME).performClick()
        awaitText("Past day")
    }

    private fun ComposeTestRule.favouriteBitcoinFromMarketDetails() {
        openBitcoinDetailsFromMarket()
        onNodeWithContentDescription("Favourite").performClick()
        onNodeWithContentDescription("Back").performClick()
    }

    private fun ComposeTestRule.openFavourites() {
        onNodeWithText("Favourites").performClick()
    }
}

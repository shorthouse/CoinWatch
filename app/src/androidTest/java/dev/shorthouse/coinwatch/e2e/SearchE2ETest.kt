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
import dev.shorthouse.coinwatch.fixture.bitcoinSearchResult
import dev.shorthouse.coinwatch.fixture.failSearchResults
import dev.shorthouse.coinwatch.fixture.respondWithSearchResults
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SearchE2ETest {

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
    fun when_searchResultClicked_should_displayCoinDetails() {
        fakeCoinNetworkDataSource.respondWithSearchResults(bitcoinSearchResult())

        composeTestRule.apply {
            openSearch()
            enterSearchQuery("Bit")
            awaitText(Bitcoin.NAME)

            onNodeWithText(Bitcoin.NAME).performClick()

            awaitText("Past day")
            onNodeWithContentDescription("Back").assertIsDisplayed()
            onNodeWithContentDescription("Favourite").assertIsDisplayed()
            onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
            onNodeWithText(Bitcoin.SYMBOL).assertIsDisplayed()
            onNodeWithText(Bitcoin.FORMATTED_PRICE).assertIsDisplayed()
        }
    }

    @Test
    fun when_searchHasNoResults_should_showEmptyState() {
        composeTestRule.apply {
            openSearch()
            enterSearchQuery("zzzzzz")

            awaitText("No coins found")
            onNodeWithText("Try adjusting your search").assertIsDisplayed()
        }
    }

    @Test
    fun when_searchFails_should_showErrorState() {
        fakeCoinNetworkDataSource.failSearchResults()

        composeTestRule.apply {
            openSearch()
            enterSearchQuery("Bit")

            awaitText("An error has occurred")
            onNodeWithText("Unable to fetch coin search results").assertIsDisplayed()
        }
    }

    @Test
    fun when_searchCleared_should_showQueryEmptyState() {
        fakeCoinNetworkDataSource.respondWithSearchResults(bitcoinSearchResult())

        composeTestRule.apply {
            openSearch()
            enterSearchQuery("Bit")
            awaitText(Bitcoin.NAME)

            onNodeWithContentDescription("Clear search").performClick()

            awaitText("Explore coins")
            onNodeWithText("Search by name or symbol").assertIsDisplayed()
        }
    }

    @Test
    fun when_searchResultsLoaded_should_preserveQueryAndResultsAcrossBottomNavigation() {
        fakeCoinNetworkDataSource.respondWithSearchResults(bitcoinSearchResult())

        composeTestRule.apply {
            openSearch()
            enterSearchQuery("Bit")
            awaitText(Bitcoin.NAME)

            onNodeWithText("Market").performClick()
            onNodeWithText("Search").performClick()

            onNodeWithText("Bit").assertIsDisplayed()
            onNodeWithText(Bitcoin.NAME).assertIsDisplayed()
        }
    }

    private fun ComposeTestRule.openSearch() {
        onNodeWithText("Search").performClick()
    }

    private fun ComposeTestRule.enterSearchQuery(query: String) {
        onNodeWithText("Search coins").performClick()
        onNodeWithText("Search coins").performTextInput(query)
    }
}

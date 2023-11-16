package dev.shorthouse.coinwatch.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.model.SearchCoin
import dev.shorthouse.coinwatch.ui.screen.search.SearchScreen
import dev.shorthouse.coinwatch.ui.screen.search.SearchUiState
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_uiStateError_should_showErrorMessage() {
        val uiState = SearchUiState(
            errorMessage = "Error message"
        )

        composeTestRule.setContent {
            AppTheme {
                SearchScreen(
                    uiState = uiState,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    onCoinClick = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("An error has occurred").assertIsDisplayed()
            onNodeWithText("Error message").assertIsDisplayed()
        }
    }

    @Test
    fun when_uiStateSearching_should_showSearchingIndicator() {
        val uiState = SearchUiState(
            isSearching = true
        )

        composeTestRule.setContent {
            AppTheme {
                SearchScreen(
                    uiState = uiState,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    onCoinClick = {}
                )
            }
        }

        composeTestRule.apply {
            onNode(
                SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun when_searchScreenDisplayed_should_showExpectedContent() {
        val uiState = SearchUiState()

        composeTestRule.setContent {
            AppTheme {
                SearchScreen(
                    uiState = uiState,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    onCoinClick = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Search coins").assertIsDisplayed()
        }
    }

    @Test
    fun when_searchQueryEntered_should_displaySearchQuery() {
        val searchQuery = "Bitcoin"

        val uiState = SearchUiState()

        composeTestRule.setContent {
            AppTheme {
                SearchScreen(
                    uiState = uiState,
                    searchQuery = searchQuery,
                    onSearchQueryChange = {},
                    onCoinClick = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Bitcoin").assertIsDisplayed()
        }
    }

    @Test
    fun when_searchQueryEntered_should_displayClearSearchButton() {
        val searchQuery = "Bitcoin"

        val uiState = SearchUiState()

        composeTestRule.setContent {
            AppTheme {
                SearchScreen(
                    uiState = uiState,
                    searchQuery = searchQuery,
                    onSearchQueryChange = {},
                    onCoinClick = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Clear search").assertIsDisplayed()
        }
    }

    @Test
    fun when_clearSearchClicked_should_clearSearchQuery() {
        val searchQuery = mutableStateOf("Bitcoin")

        val uiState = SearchUiState()

        composeTestRule.setContent {
            AppTheme {
                SearchScreen(
                    uiState = uiState,
                    searchQuery = searchQuery.value,
                    onSearchQueryChange = { searchQuery.value = it },
                    onCoinClick = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithContentDescription("Clear search").performClick()
            onNodeWithText("Search coins").assertIsDisplayed()
            onNodeWithText("Bitcoin").assertDoesNotExist()
        }
    }

    @Test
    fun when_typingInSearchBar_should_updateSearchQuery() {
        val searchQuery = mutableStateOf("")

        val uiState = SearchUiState()

        composeTestRule.setContent {
            AppTheme {
                SearchScreen(
                    uiState = uiState,
                    searchQuery = searchQuery.value,
                    onSearchQueryChange = { searchQuery.value = it },
                    onCoinClick = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Search coins").performClick()
            onNodeWithText("Bitcoin").assertDoesNotExist()
            onNodeWithText("Search coins").performTextInput("Bitcoin")
            onNodeWithText("Bitcoin").assertIsDisplayed()
        }
    }

    @Test
    fun when_searchResultsListDisplayed_should_displayInExpectedFormat() {
        val uiState = SearchUiState(
            searchResults = persistentListOf(
                SearchCoin(
                    id = "Qwsogvtv82FCd",
                    symbol = "BTC",
                    name = "Bitcoin",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg"
                ),
                SearchCoin(
                    id = "ZlZpzOJo43mIo",
                    symbol = "BCH",
                    name = "Bitcoin Cash",
                    imageUrl = "https://cdn.coinranking.com/By8ziihX7/bch.svg"
                )
            )
        )

        composeTestRule.setContent {
            AppTheme {
                SearchScreen(
                    uiState = uiState,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    onCoinClick = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Bitcoin").assertIsDisplayed()
            onNodeWithText("BTC").assertIsDisplayed()
            onNodeWithText("Bitcoin Cash").assertIsDisplayed()
            onNodeWithText("BCH").assertIsDisplayed()
        }
    }

    @Test
    fun when_clickingCoinItem_should_callOnCoinClick() {
        var onCoinClickCalled = false

        val uiState = SearchUiState(
            searchResults = persistentListOf(
                SearchCoin(
                    id = "Qwsogvtv82FCd",
                    symbol = "BTC",
                    name = "Bitcoin",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg"
                )
            )
        )

        composeTestRule.setContent {
            AppTheme {
                SearchScreen(
                    uiState = uiState,
                    searchQuery = "",
                    onSearchQueryChange = {},
                    onCoinClick = { onCoinClickCalled = true }
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Bitcoin").performClick()
        }

        assertThat(onCoinClickCalled).isTrue()
    }

    @Test
    fun when_searchQueryHasNoResults_should_displaySearchEmptyState() {
        val uiState = SearchUiState(
            queryHasNoResults = true
        )

        composeTestRule.setContent {
            AppTheme {
                SearchScreen(
                    uiState = uiState,
                    searchQuery = "abcdefghijk",
                    onSearchQueryChange = {},
                    onCoinClick = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("No coins found").assertIsDisplayed()
            onNodeWithText("Try adjusting your search").assertIsDisplayed()
        }
    }
}

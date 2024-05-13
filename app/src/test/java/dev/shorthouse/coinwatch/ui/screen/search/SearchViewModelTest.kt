package dev.shorthouse.coinwatch.ui.screen.search

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.domain.search.GetCoinSearchResultsUseCase
import dev.shorthouse.coinwatch.model.SearchCoin
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.collections.immutable.persistentListOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var viewModel: SearchViewModel

    @MockK
    private lateinit var getCoinSearchResultsUseCase: GetCoinSearchResultsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = SearchViewModel(
            getCoinSearchResultsUseCase = getCoinSearchResultsUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When ViewModel is initialised should have empty search query`() {
        // Arrange
        val expectedSearchQuery = ""

        // Act

        // Assert
        assertThat(viewModel.searchQuery).isEqualTo(expectedSearchQuery)
    }

    @Test
    fun `When search query is empty should return empty search results list`() {
        // Arrange
        val expectedUiState = SearchUiState()

        // Act
        viewModel.updateSearchQuery("")
        viewModel.initialiseUiState()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When search query results returns error should have error UI state`() {
        // Arrange
        val searchQuery = "bit"
        val errorMessage = "Unable to fetch coin search results"

        val expectedUiState = SearchUiState(
            errorMessage = errorMessage
        )

        coEvery { getCoinSearchResultsUseCase(searchQuery) } returns
            Result.Error(
                "Unable to fetch coin search results"
            )

        // Act
        viewModel.updateSearchQuery(searchQuery)
        viewModel.initialiseUiState()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When search query results returns success should have success UI state`() {
        // Arrange
        val searchQuery = "bit"

        val searchResults = persistentListOf(
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

        val expectedUiState = SearchUiState(
            searchResults = searchResults
        )

        coEvery { getCoinSearchResultsUseCase(searchQuery) } returns
            Result.Success(
                listOf(
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

        // Act
        viewModel.updateSearchQuery(searchQuery)
        viewModel.initialiseUiState()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When search query results returns success with empty list should set no results flag`() {
        // Arrange
        val searchQuery = "abcdefghijk"

        val searchResults = persistentListOf<SearchCoin>()

        val expectedUiState = SearchUiState(
            searchResults = searchResults,
            queryHasNoResults = true
        )

        coEvery {
            getCoinSearchResultsUseCase(searchQuery)
        } returns Result.Success(emptyList())

        // Act
        viewModel.updateSearchQuery(searchQuery)
        viewModel.initialiseUiState()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When updating search query should update search query value`() {
        // Arrange
        val searchQuery = "bit"

        coEvery { getCoinSearchResultsUseCase(searchQuery) } returns Result.Success(emptyList())

        // Act
        viewModel.updateSearchQuery(searchQuery)

        // Assert
        assertThat(viewModel.searchQuery).isEqualTo(searchQuery)
    }
}

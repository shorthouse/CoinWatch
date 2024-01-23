package dev.shorthouse.coinwatch.ui.screen.favourites

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.domain.GetFavouriteCoinsUseCaseOld
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavouritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var viewModel: FavouritesViewModel

    @RelaxedMockK
    private lateinit var getFavouriteCoinsUseCaseOld: GetFavouriteCoinsUseCaseOld

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = FavouritesViewModel(
            getFavouriteCoinsUseCaseOld = getFavouriteCoinsUseCaseOld
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When ViewModel is created should have loading UI state`() {
        // Arrange
        val expectedUiState = FavouritesUiState(isLoading = true)

        // Act
        val uiState = viewModel.uiState.value

        // Assert
        assertThat(uiState).isEqualTo(expectedUiState)
    }

    @Test
    fun `When favourite coins returns error should have error UI state`() {
        // Arrange
        val errorMessage = "Error message"
        val expectedUiState = FavouritesUiState(errorMessage = errorMessage)

        every { getFavouriteCoinsUseCaseOld() } returns flowOf(Result.Error(errorMessage))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When favourite coins returns success should have success UI state`() {
        // Arrange
        val expectedUiState = FavouritesUiState(
            favouriteCoins = persistentListOf()
        )

        every { getFavouriteCoinsUseCaseOld() } returns flowOf(Result.Success(emptyList()))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }
}

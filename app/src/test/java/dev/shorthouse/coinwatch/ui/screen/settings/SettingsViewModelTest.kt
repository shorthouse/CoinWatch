package dev.shorthouse.coinwatch.ui.screen.settings

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.data.userPreferences.StartScreen
import dev.shorthouse.coinwatch.data.userPreferences.UserPreferences
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.UpdateStartScreenUseCase
import io.mockk.MockKAnnotations
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import java.io.IOException
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var viewModel: SettingsViewModel

    @RelaxedMockK
    private lateinit var getUserPreferencesUseCase: GetUserPreferencesUseCase

    @RelaxedMockK
    private lateinit var updateStartScreenUseCase: UpdateStartScreenUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = SettingsViewModel(
            getUserPreferencesUseCase = getUserPreferencesUseCase,
            updateStartScreenUseCase = updateStartScreenUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When ViewModel is created should have loading UI state`() {
        // Arrange
        val expectedUiState = SettingsUiState(isLoading = true)

        // Act
        val uiState = viewModel.uiState.value

        // Assert
        assertThat(uiState).isEqualTo(expectedUiState)
    }

    @Test
    fun `When user preferences returns error should have error UI state`() {
        // Arrange
        val errorMessage = "Error getting user preferences"
        val expectedUiState = SettingsUiState(
            errorMessage = errorMessage,
            isLoading = false
        )

        every { getUserPreferencesUseCase() } returns flow {
            throw IOException(errorMessage)
        }

        // Act
        viewModel.initialiseUiState()
        val uiState = viewModel.uiState.value

        // Assert
        assertThat(uiState).isEqualTo(expectedUiState)
    }

    @Test
    fun `When user preferences returns success should set expected start screen`() {
        // Arrange
        val expectedUiState = SettingsUiState(
            startScreen = StartScreen.Search,
            isLoading = false,
            errorMessage = null
        )

        every { getUserPreferencesUseCase() } returns flowOf(
            UserPreferences(startScreen = StartScreen.Search)
        )

        // Act
        viewModel.initialiseUiState()
        val uiState = viewModel.uiState.value

        // Assert
        assertThat(uiState).isEqualTo(expectedUiState)
    }

    @Test
    fun `When update start screen called should call update start screen use case`() {
        // Arrange
        val startScreen = StartScreen.Favourites

        // Act
        viewModel.updateStartScreen(startScreen)

        // Assert
        coVerifySequence {
            updateStartScreenUseCase(startScreen)
        }
    }
}

package dev.shorthouse.coinwatch.ui.screen.settings

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.data.preferences.global.StartScreen
import dev.shorthouse.coinwatch.data.preferences.global.UserPreferences
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.UpdateCurrencyUseCase
import dev.shorthouse.coinwatch.domain.UpdateStartScreenUseCase
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var viewModel: SettingsViewModel

    @RelaxedMockK
    private lateinit var getUserPreferencesUseCase: GetUserPreferencesUseCase

    @RelaxedMockK
    private lateinit var updateStartScreenUseCase: UpdateStartScreenUseCase

    @RelaxedMockK
    private lateinit var updateCurrencyUseCase: UpdateCurrencyUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = SettingsViewModel(
            getUserPreferencesUseCase = getUserPreferencesUseCase,
            updateCurrencyUseCase = updateCurrencyUseCase,
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
    fun `When user preferences returns success should set expected fields`() {
        // Arrange
        val expectedUiState = SettingsUiState(
            currency = Currency.GBP,
            startScreen = StartScreen.Search,
            isLoading = false,
            errorMessage = null
        )

        every { getUserPreferencesUseCase() } returns flowOf(
            UserPreferences(
                currency = Currency.GBP,
                startScreen = StartScreen.Search
            )
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

    @Test
    fun `When update currency called should call update currency use case`() {
        // Arrange
        val currency = Currency.GBP

        // Act
        viewModel.updateCurrency(currency)

        // Assert
        coVerify {
            updateCurrencyUseCase(currency)
        }
    }

    @Test
    fun `When update show coin currency bottom sheet called should update UI state`() {
        // Arrange
        val currentShowSheet = viewModel.uiState.value.isCurrencySheetShown
        val newShowSheet = currentShowSheet.not()

        // Act
        viewModel.updateIsCurrencySheetShown(newShowSheet)

        // Assert
        assertThat(viewModel.uiState.value.isCurrencySheetShown).isEqualTo(newShowSheet)
    }

//    @Test
//    fun `When showing currency sheet with another sheet already open should not show sheet`() {
//        // Arrange
//
//        // Act
//        viewModel.updateIsCoinSortSheetShown(true)
//        viewModel.updateIsCurrencySheetShown(true)
//
//        // Assert
//        assertThat(viewModel.uiState.value.isCurrencySheetShown).isFalse()
//    }
    
    //TODO do the same test for new start screen sheet
}

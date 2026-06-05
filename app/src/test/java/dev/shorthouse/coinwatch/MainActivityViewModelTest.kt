package dev.shorthouse.coinwatch

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.StartScreen
import dev.shorthouse.coinwatch.data.source.local.datastore.global.UserPreferences
import dev.shorthouse.coinwatch.domain.preferences.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.navigation.NavigationBarScreen
import dev.shorthouse.coinwatch.rule.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var getUserPreferencesUseCase: GetUserPreferencesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When start screen is Market should map to Market navigation screen`() {
        val viewModel = createViewModel(startScreen = StartScreen.Market)

        assertThat(viewModel.uiState.value).isEqualTo(
            MainActivityUiState(
                startScreen = NavigationBarScreen.Market,
                isLoading = false
            )
        )
    }

    @Test
    fun `When start screen is Favourites should map to Favourites navigation screen`() {
        val viewModel = createViewModel(startScreen = StartScreen.Favourites)

        assertThat(viewModel.uiState.value).isEqualTo(
            MainActivityUiState(
                startScreen = NavigationBarScreen.Favourites,
                isLoading = false
            )
        )
    }

    @Test
    fun `When start screen is Pulse should map to Pulse navigation screen`() {
        val viewModel = createViewModel(startScreen = StartScreen.Pulse)

        assertThat(viewModel.uiState.value).isEqualTo(
            MainActivityUiState(
                startScreen = NavigationBarScreen.Pulse,
                isLoading = false
            )
        )
    }

    @Test
    fun `When start screen is Search should map to Search navigation screen`() {
        val viewModel = createViewModel(startScreen = StartScreen.Search)

        assertThat(viewModel.uiState.value).isEqualTo(
            MainActivityUiState(
                startScreen = NavigationBarScreen.Search,
                isLoading = false
            )
        )
    }

    private fun createViewModel(startScreen: StartScreen): MainActivityViewModel {
        every { getUserPreferencesUseCase() } returns flowOf(
            UserPreferences(startScreen = startScreen)
        )

        return MainActivityViewModel(
            getUserPreferencesUseCase = getUserPreferencesUseCase
        )
    }
}

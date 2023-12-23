package dev.shorthouse.coinwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.data.userPreferences.StartScreen
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.navigation.NavigationBarScreen
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    private fun initialiseUiState() {
        _uiState.update { it.copy(isLoading = true) }

        getUserPreferencesUseCase()
            .onEach { userPreferences ->
                _uiState.update {
                    it.copy(
                        startScreen = when (userPreferences.startScreen) {
                            StartScreen.Market -> NavigationBarScreen.Market
                            StartScreen.Favourites -> NavigationBarScreen.Favourites
                            StartScreen.Search -> NavigationBarScreen.Search
                        },
                        isLoading = false
                    )
                }
            }.catch {
                _uiState.update { it.copy(isLoading = false) }
            }.launchIn(viewModelScope)
    }
}

package dev.shorthouse.coinwatch.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.data.preferences.global.StartScreen
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.UpdateCurrencyUseCase
import dev.shorthouse.coinwatch.domain.UpdateStartScreenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val updateStartScreenUseCase: UpdateStartScreenUseCase,
    private val updateCurrencyUseCase: UpdateCurrencyUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        val userPreferencesFlow = getUserPreferencesUseCase()

        userPreferencesFlow.onEach { userPreferences ->
            _uiState.update {
                it.copy(
                    startScreen = userPreferences.startScreen,
                    currency = userPreferences.currency,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }.catch {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Error getting user preferences"
                )
            }
        }.launchIn(viewModelScope)
    }

    fun updateCurrency(currency: Currency) {
        viewModelScope.launch {
            updateCurrencyUseCase(currency = currency)
        }
    }

    fun updateStartScreen(startScreen: StartScreen) {
        viewModelScope.launch {
            updateStartScreenUseCase(startScreen = startScreen)
        }
    }

    fun updateIsCurrencySheetShown(showSheet: Boolean) {
        if (isAnyBottomSheetOpen() && showSheet) return

        _uiState.update { it.copy(isCurrencySheetShown = showSheet) }
    }

    fun updateIsStartScreenSheetShown(showSheet: Boolean) {
        if (isAnyBottomSheetOpen() && showSheet) return

        _uiState.update { it.copy(isStartScreenSheetShown = showSheet) }
    }

    private fun isAnyBottomSheetOpen(): Boolean {
        return _uiState.value.isCurrencySheetShown || _uiState.value.isStartScreenSheetShown
    }
}

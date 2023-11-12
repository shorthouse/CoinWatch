package dev.shorthouse.coinwatch.ui.screen.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.data.datastore.Currency
import dev.shorthouse.coinwatch.domain.GetCachedCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.RefreshCachedCoinsUseCase
import dev.shorthouse.coinwatch.domain.UpdateCoinSortUseCase
import dev.shorthouse.coinwatch.domain.UpdateCurrencyUseCase
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import java.time.LocalTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getCachedCoinsUseCase: GetCachedCoinsUseCase,
    private val refreshCachedCoinsUseCase: RefreshCachedCoinsUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val updateCoinSortUseCase: UpdateCoinSortUseCase,
    private val updateCurrencyUseCase: UpdateCurrencyUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MarketUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        getUserPreferencesUseCase().onEach { userPreferences ->
            _uiState.update {
                it.copy(
                    coinSort = userPreferences.coinSort,
                    currency = userPreferences.currency
                )
            }

            refreshCachedCoinsUseCase(
                coinSort = userPreferences.coinSort,
                currency = userPreferences.currency
            )
        }.launchIn(viewModelScope)

        getCachedCoinsUseCase().onEach { coinsResult ->
            when (coinsResult) {
                is Result.Success -> {
                    val coins = coinsResult.data.toImmutableList()

                    _uiState.update {
                        it.copy(
                            coins = coins,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = coinsResult.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)

        getCurrentHourFlow().onEach { currentHour ->
            val timeOfDay = when (currentHour) {
                in 4..11 -> TimeOfDay.Morning
                in 12..17 -> TimeOfDay.Afternoon
                else -> TimeOfDay.Evening
            }

            _uiState.update {
                it.copy(timeOfDay = timeOfDay)
            }
        }.launchIn(viewModelScope)
    }

    fun pullRefreshCachedCoins() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRefreshing = true,
                    isLoading = true
                )
            }

            val userPreferences = getUserPreferencesUseCase().first()
            refreshCachedCoinsUseCase(
                coinSort = userPreferences.coinSort,
                currency = userPreferences.currency
            )

            _uiState.update {
                it.copy(
                    isRefreshing = false,
                    isLoading = false
                )
            }
        }
    }

    fun updateCoinSort(coinSort: CoinSort) {
        viewModelScope.launch {
            updateCoinSortUseCase(coinSort = coinSort)
        }
    }

    fun updateCurrency(currency: Currency) {
        viewModelScope.launch {
            updateCurrencyUseCase(currency = currency)
        }
    }

    fun updateShowCoinSortBottomSheet(showSheet: Boolean) {
        if (showSheet && isAnyBottomSheetOpen()) return

        _uiState.update { it.copy(showCoinSortBottomSheet = showSheet) }
    }

    fun onUpdateShowCurrencyBottomSheet(showSheet: Boolean) {
        if (showSheet && isAnyBottomSheetOpen()) return

        _uiState.update { it.copy(showCurrencyBottomSheet = showSheet) }
    }

    private fun isAnyBottomSheetOpen(): Boolean {
        return _uiState.value.showCoinSortBottomSheet || _uiState.value.showCurrencyBottomSheet
    }

    private fun getCurrentHourFlow(): Flow<Int> = flow {
        while (true) {
            val currentHour = LocalTime.now().hour
            emit(currentHour)
            delay(5.minutes.inWholeMilliseconds)
        }
    }
}

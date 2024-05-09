package dev.shorthouse.coinwatch.ui.screen.market

import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.domain.GetCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetMarketPreferencesUseCase
import dev.shorthouse.coinwatch.domain.GetMarketStatsUseCase
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.UpdateCachedCoinsUseCase
import dev.shorthouse.coinwatch.domain.UpdateMarketCoinSortUseCase
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
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
import java.time.LocalTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getMarketStatsUseCase: GetMarketStatsUseCase,
    private val updateCachedCoinsUseCase: UpdateCachedCoinsUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val getMarketPreferencesUseCase: GetMarketPreferencesUseCase,
    private val updateMarketCoinSortUseCase: UpdateMarketCoinSortUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MarketUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        _uiState.update { it.copy(isLoading = true) }

        getCoinsUseCase().onEach { coinsResult ->
            when (coinsResult) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            coins = coinsResult.data.toImmutableList(),
                            isLoading = false
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        val errorMessages = it.errorMessageIds + R.string.error_local_coins

                        it.copy(
                            errorMessageIds = errorMessages,
                            isLoading = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)

        getUserPreferencesUseCase().onEach { userPreferences ->
            updateCachedCoins(
                coinSort = getMarketPreferencesUseCase().first().coinSort,
                currency = userPreferences.currency
            )
        }.launchIn(viewModelScope)

        getMarketPreferencesUseCase().onEach { marketPreferences ->
            _uiState.update {
                it.copy(
                    coinSort = marketPreferences.coinSort
                )
            }

            updateCachedCoins(
                coinSort = marketPreferences.coinSort,
                currency = getUserPreferencesUseCase().first().currency
            )
        }.launchIn(viewModelScope)

        getCurrentHourFlow().onEach { currentHour ->
            val timeOfDay = calculateTimeOfDay(hour = currentHour)

            _uiState.update {
                it.copy(timeOfDay = timeOfDay)
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            val marketStatsResult = getMarketStatsUseCase()

            when (marketStatsResult) {
                is Result.Success -> {
                    val marketStats = marketStatsResult.data

                    _uiState.update {
                        it.copy(
                            marketCapChangePercentage24h = marketStats.marketCapChangePercentage24h
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        val errorMessages = it.errorMessageIds + R.string.error_market_stats

                        it.copy(
                            errorMessageIds = errorMessages
                        )
                    }
                }
            }
        }
    }

    fun pullRefreshCoins() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            delay(250.milliseconds)

            val userPreferences = getUserPreferencesUseCase().first()
            val marketPreferences = getMarketPreferencesUseCase().first()

            updateCachedCoins(
                coinSort = marketPreferences.coinSort,
                currency = userPreferences.currency
            )

            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun updateCoinSort(coinSort: CoinSort) {
        viewModelScope.launch {
            updateMarketCoinSortUseCase(coinSort = coinSort)
        }
    }

    fun dismissErrorMessage(@StringRes dismissedErrorMessageId: Int) {
        _uiState.update {
            val errorMessageIds = it.errorMessageIds.filter { errorMessageId ->
                errorMessageId != dismissedErrorMessageId
            }
            it.copy(errorMessageIds = errorMessageIds)
        }
    }

    @VisibleForTesting
    fun calculateTimeOfDay(hour: Int): TimeOfDay {
        return when (hour) {
            in 4..11 -> TimeOfDay.Morning
            in 12..17 -> TimeOfDay.Afternoon
            else -> TimeOfDay.Evening
        }
    }

    private fun getCurrentHourFlow(): Flow<Int> = flow {
        while (true) {
            val currentHour = LocalTime.now().hour
            emit(currentHour)
            delay(5.minutes.inWholeMilliseconds)
        }
    }

    private suspend fun updateCachedCoins(coinSort: CoinSort, currency: Currency) {
        val result = updateCachedCoinsUseCase(coinSort = coinSort, currency = currency)

        if (result is Result.Error) {
            _uiState.update {
                val errorMessages = it.errorMessageIds + R.string.error_network_coins
                it.copy(errorMessageIds = errorMessages)
            }
        }
    }
}

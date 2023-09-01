package dev.shorthouse.coinwatch.ui.screen.list

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.domain.GetCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetFavouriteCoinsUseCase
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getFavouriteCoinsUseCase: GetFavouriteCoinsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<CoinListUiState>(CoinListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        _uiState.update { CoinListUiState.Loading }

        val coinsFlow = getCoinsUseCase()
        val favouriteCoinsFlow = getFavouriteCoinsUseCase()
        val currentHourFlow = flow {
            emit(LocalTime.now().hour)
            delay(5.minutes.inWholeMilliseconds)
        }

        combine(
            coinsFlow,
            favouriteCoinsFlow,
            currentHourFlow
        ) { coinsResult, favouriteCoinsResult, currentHour ->
            when {
                coinsResult is Result.Error -> {
                    _uiState.update { CoinListUiState.Error(coinsResult.message) }
                }
                favouriteCoinsResult is Result.Error -> {
                    _uiState.update { CoinListUiState.Error(favouriteCoinsResult.message) }
                }
                coinsResult is Result.Success && favouriteCoinsResult is Result.Success -> {
                    val coins = coinsResult.data

                    val favouriteCoinIds = favouriteCoinsResult.data.map { favouriteCoin ->
                        favouriteCoin.id
                    }

                    val favouriteCoins = coins.filter { coin ->
                        coin.id in favouriteCoinIds
                    }

                    _uiState.update {
                        CoinListUiState.Success(
                            coins = coins.toImmutableList(),
                            favouriteCoins = favouriteCoins.toImmutableList(),
                            timeOfDay = calculateTimeOfDay(currentHour)
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun calculateTimeOfDay(currentHour: Int): TimeOfDay {
        return when (currentHour) {
            in 0..11 -> TimeOfDay.Morning
            in 12..17 -> TimeOfDay.Afternoon
            else -> TimeOfDay.Evening
        }
    }
}

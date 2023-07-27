package dev.shorthouse.coinwatch.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.domain.GetCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetFavouriteCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetMarketStatsUseCase
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getMarketStatsUseCase: GetMarketStatsUseCase,
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
        val marketStatsFlow = getMarketStatsUseCase()
        val favouriteCoinsFlow = getFavouriteCoinsUseCase()

        combine(
            coinsFlow,
            marketStatsFlow,
            favouriteCoinsFlow
        ) { coinsResult, marketStatsResult, favouriteCoinsResult ->
            when {
                coinsResult is Result.Error -> {
                    _uiState.update { CoinListUiState.Error(coinsResult.message) }
                }
                marketStatsResult is Result.Error -> {
                    _uiState.update { CoinListUiState.Error(marketStatsResult.message) }
                }
                favouriteCoinsResult is Result.Error -> {
                    _uiState.update { CoinListUiState.Error(favouriteCoinsResult.message) }
                }
                coinsResult is Result.Success &&
                    marketStatsResult is Result.Success &&
                    favouriteCoinsResult is Result.Success -> {
                    val favouriteCoinIds = favouriteCoinsResult.data.map { favouriteCoin ->
                        favouriteCoin.id
                    }

                    val coins = coinsResult.data

                    val favouriteCoins = coins.filter { coin ->
                        coin.id in favouriteCoinIds
                    }

                    Timber.d("yosh ${coins.take(5)}")

                    _uiState.update {
                        CoinListUiState.Success(
                            coins = coins,
                            favouriteCoins = favouriteCoins,
                            marketStats = marketStatsResult.data,
                            timeOfDay = calculateTimeOfDay()
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun calculateTimeOfDay(): TimeOfDay {
        return when (LocalDateTime.now().hour) {
            in 0..11 -> TimeOfDay.Morning
            in 12..17 -> TimeOfDay.Afternoon
            else -> TimeOfDay.Evening
        }
    }
}

package dev.shorthouse.cryptodata.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.domain.GetCoinsUseCase
import dev.shorthouse.cryptodata.domain.GetFavouriteCoinsUseCase
import dev.shorthouse.cryptodata.domain.GetMarketStatsUseCase
import dev.shorthouse.cryptodata.ui.model.TimeOfDay
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

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

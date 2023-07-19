package dev.shorthouse.cryptodata.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.domain.GetCoinsUseCase
import dev.shorthouse.cryptodata.domain.GetMarketStatsUseCase
import dev.shorthouse.cryptodata.ui.model.TimeOfDay
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
    private val getMarketStatsUseCase: GetMarketStatsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<CoinListUiState>(CoinListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    private fun initialiseUiState() {
        val coinsFlow = getCoinsUseCase()
        val marketStatsFlow = getMarketStatsUseCase()

        combine(coinsFlow, marketStatsFlow) { coinsResult, marketStatsResult ->
            when {
                coinsResult is Result.Error -> {
                    Timber.e("getCoins error ${coinsResult.message}")
                    _uiState.update { CoinListUiState.Error(coinsResult.message) }
                }
                marketStatsResult is Result.Error -> {
                    Timber.e("getMarketStats error ${marketStatsResult.message}")
                    _uiState.update { CoinListUiState.Error(marketStatsResult.message) }
                }
                coinsResult is Result.Success && marketStatsResult is Result.Success -> {
                    val timeOfDay = when (LocalDateTime.now().hour) {
                        in 0..11 -> TimeOfDay.Morning
                        in 12..17 -> TimeOfDay.Afternoon
                        else -> TimeOfDay.Evening
                    }

                    _uiState.update {
                        CoinListUiState.Success(
                            coins = coinsResult.data,
                            marketStats = marketStatsResult.data,
                            timeOfDay = timeOfDay
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

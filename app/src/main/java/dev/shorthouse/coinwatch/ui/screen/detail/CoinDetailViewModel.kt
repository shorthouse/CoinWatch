package dev.shorthouse.coinwatch.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Constants.PARAM_COIN_ID
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.domain.DeleteFavouriteCoinUseCase
import dev.shorthouse.coinwatch.domain.GetCoinChartUseCase
import dev.shorthouse.coinwatch.domain.GetCoinDetailUseCase
import dev.shorthouse.coinwatch.domain.InsertFavouriteCoinUseCase
import dev.shorthouse.coinwatch.domain.IsCoinFavouriteUseCase
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCoinDetailUseCase: GetCoinDetailUseCase,
    private val getCoinChartUseCase: GetCoinChartUseCase,
    private val isCoinFavouriteUseCase: IsCoinFavouriteUseCase,
    private val insertFavouriteCoinUseCase: InsertFavouriteCoinUseCase,
    private val deleteFavouriteCoinUseCase: DeleteFavouriteCoinUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<CoinDetailUiState>(CoinDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val chartPeriodFlow = MutableStateFlow(ChartPeriod.Day)
    private val coinId = savedStateHandle.get<String>(PARAM_COIN_ID)

    init {
        initialiseUiState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun initialiseUiState() {
        _uiState.update { CoinDetailUiState.Loading }

        if (coinId == null) {
            _uiState.update { CoinDetailUiState.Error("Invalid coin ID") }
            return
        }

        val coinDetailFlow = getCoinDetailUseCase(coinId = coinId)
        val coinChartFlow = chartPeriodFlow.flatMapLatest { chartPeriod ->
            getCoinChartUseCase(
                coinId = coinId,
                chartPeriod = chartPeriod.stringName
            )
        }
        val isCoinFavouriteFlow = isCoinFavouriteUseCase(coinId = coinId)

        combine(
            coinDetailFlow,
            coinChartFlow,
            isCoinFavouriteFlow
        ) { coinDetailResult, coinChartResult, isCoinFavouriteResult ->
            when {
                coinDetailResult is Result.Error -> {
                    _uiState.update { CoinDetailUiState.Error(coinDetailResult.message) }
                }
                coinChartResult is Result.Error -> {
                    _uiState.update { CoinDetailUiState.Error(coinChartResult.message) }
                }
                isCoinFavouriteResult is Result.Error -> {
                    _uiState.update { CoinDetailUiState.Error(isCoinFavouriteResult.message) }
                }
                coinDetailResult is Result.Success &&
                    coinChartResult is Result.Success &&
                    isCoinFavouriteResult is Result.Success -> {
                    _uiState.update {
                        CoinDetailUiState.Success(
                            coinDetail = coinDetailResult.data,
                            coinChart = coinChartResult.data,
                            chartPeriod = chartPeriodFlow.value,
                            isCoinFavourite = isCoinFavouriteResult.data
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateChartPeriod(chartPeriod: ChartPeriod) {
        chartPeriodFlow.value = chartPeriod
    }

    fun toggleIsCoinFavourite() {
        viewModelScope.launch {
            if (coinId != null) {
                val isCoinFavouriteResult = isCoinFavouriteUseCase(coinId).first()

                if (isCoinFavouriteResult is Result.Success) {
                    val isCoinFavourite = isCoinFavouriteResult.data

                    if (isCoinFavourite) {
                        deleteFavouriteCoinUseCase(
                            FavouriteCoin(id = coinId)
                        )
                    } else {
                        insertFavouriteCoinUseCase(
                            FavouriteCoin(id = coinId)
                        )
                    }
                }
            }
        }
    }
}

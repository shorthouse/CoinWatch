package dev.shorthouse.cryptodata.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.cryptodata.common.Constants.PARAM_COIN_ID
import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.local.model.FavouriteCoin
import dev.shorthouse.cryptodata.domain.DeleteFavouriteCoinUseCase
import dev.shorthouse.cryptodata.domain.GetCoinChartUseCase
import dev.shorthouse.cryptodata.domain.GetCoinDetailUseCase
import dev.shorthouse.cryptodata.domain.InsertFavouriteCoinUseCase
import dev.shorthouse.cryptodata.domain.IsCoinFavouriteUseCase
import dev.shorthouse.cryptodata.ui.model.ChartPeriod
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        coinId?.let {
            initialiseUiState(coinId = coinId)
        }
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

    private fun initialiseUiState(coinId: String) {
        val coinDetailFlow = getCoinDetailUseCase(coinId = coinId)
        val coinChartFlow = chartPeriodFlow.flatMapLatest { chartPeriod ->
            getCoinChartUseCase(
                coinId = coinId,
                chartPeriodDays = chartPeriod.stringName
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
}

package dev.shorthouse.cryptodata.ui.screen.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.cryptodata.common.Constants.PARAM_COIN_ID
import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.domain.GetCoinChartUseCase
import dev.shorthouse.cryptodata.domain.GetCoinDetailUseCase
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCoinDetailUseCase: GetCoinDetailUseCase,
    private val getCoinChartUseCase: GetCoinChartUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val chartPeriodFlow = MutableStateFlow(7.days)

    init {
        savedStateHandle.get<String>(PARAM_COIN_ID)?.let { coinId ->
            getCoinDetail(coinId = coinId)
        }
    }

    fun updateChartPeriod(chartPeriod: Duration) {
        chartPeriodFlow.value = chartPeriod
    }

    private fun getCoinDetail(coinId: String) {
        val coinDetailFlow = getCoinDetailUseCase(coinId = coinId)

        combine(coinDetailFlow, chartPeriodFlow) { coinDetailResult, chartPeriod ->
            getCoinChartUseCase(
                coinId = coinId,
                chartPeriodDays = chartPeriod.inWholeDays.toString()
            ).onEach { coinChartResult ->
                if (coinDetailResult is Result.Success && coinChartResult is Result.Success) {
                    _uiState.update {
                        DetailUiState.Success(
                            coinDetail = coinDetailResult.data!!,
                            coinChart = coinChartResult.data!!,
                            chartPeriod = chartPeriod
                        )
                    }
                } else {
                    Log.d("HDS", "error coccured with coin detail or coin chart result")
                }
            }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }
}

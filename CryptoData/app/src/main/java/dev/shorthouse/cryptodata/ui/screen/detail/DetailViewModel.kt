package dev.shorthouse.cryptodata.ui.screen.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.cryptodata.common.Constants
import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.domain.GetCoinDetailUseCase
import dev.shorthouse.cryptodata.domain.GetCoinPastPricesUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCoinDetailUseCase: GetCoinDetailUseCase,
    private val getCoinPastPricesUseCase: GetCoinPastPricesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let { coinId ->
            getCoinDetail(coinId = coinId)
            getCoinPastPrices(coinId = coinId, periodDays = "7")
        }
    }

    fun getCoinPastPrices(coinId: String, periodDays: String) {
        getCoinPastPricesUseCase(coinId = coinId, periodDays = periodDays).onEach { result ->
            when (result) {
                is Result.Loading -> Log.d(
                    "HDS",
                    "loading"
                )
                is Result.Error -> Log.d("HDS", "error")
                is Result.Success -> {
                    Log.d("HDS", "success")
                    Log.d("HDS", result.data.toString())
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getCoinDetail(coinId: String) {
        getCoinDetailUseCase(coinId = coinId).onEach { result ->

            _uiState.update {
                when (result) {
                    is Result.Loading -> {
                        it.copy(
                            isLoading = true
                        )
                    }

                    is Result.Success -> {
                        it.copy(
                            coinDetail = result.data,
                            isLoading = false
                        )
                    }

                    is Result.Error -> {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

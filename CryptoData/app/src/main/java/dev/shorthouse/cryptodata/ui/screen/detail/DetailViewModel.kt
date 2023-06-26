package dev.shorthouse.cryptodata.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.cryptodata.common.Constants
import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.domain.GetCoinDetailUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCoinDetailUseCase: GetCoinDetailUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private lateinit var coinId: String

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let { coinId ->
            this.coinId = coinId
            getCoinDetail(coinId = coinId, periodDays = "14")
        }
    }

    private fun getCoinDetail(coinId: String, periodDays: String) {
        _uiState.update { DetailUiState.Loading }

        getCoinDetailUseCase(coinId = coinId, periodDays = periodDays).onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        DetailUiState.Success(result.data)
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        DetailUiState.Error
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

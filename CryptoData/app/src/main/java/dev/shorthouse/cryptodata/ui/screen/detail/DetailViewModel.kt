package dev.shorthouse.cryptodata.ui.screen.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.cryptodata.common.Constants
import dev.shorthouse.cryptodata.common.Resource
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
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_ID)?.let { cryptocurrencyId ->
            getCryptocurrencyDetail(cryptocurrencyId)
        }
    }

    private fun getCryptocurrencyDetail(cryptocurrencyId: String) {
        Log.d("HDS", "got to here 1.5")

        getCoinDetailUseCase().onEach { result ->
            Log.d("HDS", result.message.toString())
            Log.d("HDS", "got to here 2")

            _uiState.update {
                when (result) {
                    is Resource.Loading -> {
                        it.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        it.copy(
                            coinDetail = result.data,
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        it.copy(
                            error = result.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

package dev.shorthouse.cryptodata.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.domain.GetCoinsUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<CoinListUiState>(CoinListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getCoins()
    }

    private fun getCoins() {
        getCoinsUseCase().onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update { CoinListUiState.Success(result.data) }
                }
                is Result.Error -> {
                    Timber.e("getCoins error ${result.message}")
                    _uiState.update { CoinListUiState.Error(result.message) }
                }
            }
        }.launchIn(viewModelScope)
    }
}

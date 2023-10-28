package dev.shorthouse.coinwatch.ui.screen.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.domain.GetCoinsUseCase
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<MarketUiState>(MarketUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        _uiState.update { MarketUiState.Loading }

        val coinsFlow = getCoinsUseCase()

        coinsFlow.onEach { coinsResult ->
            when (coinsResult) {
                is Result.Error -> {
                    _uiState.update { MarketUiState.Error(coinsResult.message) }
                }

                is Result.Success -> {
                    val coins = coinsResult.data.toImmutableList()

                    _uiState.update {
                        MarketUiState.Success(
                            coins = coins
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

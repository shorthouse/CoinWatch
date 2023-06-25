package dev.shorthouse.cryptodata.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.cryptodata.common.Resource
import dev.shorthouse.cryptodata.domain.GetCoinsUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getCryptocurrenciesUseCase: GetCoinsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCryptocurrencies()
    }

    private fun getCryptocurrencies() {
        getCryptocurrenciesUseCase().onEach { result ->
            _uiState.update {
                when (result) {
                    is Resource.Loading -> {
                        it.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        it.copy(
                            cryptocurrencies = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        it.copy(
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

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

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCoins()
    }

    private fun getCoins() {
        getCoinsUseCase().onEach { result ->
            _uiState.update {
                when (result) {
                    is dev.shorthouse.cryptodata.common.Resource.Result.Loading -> {
                        it.copy(
                            isLoading = true
                        )
                    }

                    is dev.shorthouse.cryptodata.common.Resource.Result.Success -> {
                        it.copy(
                            coins = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }

                    is dev.shorthouse.cryptodata.common.Resource.Result.Error -> {
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

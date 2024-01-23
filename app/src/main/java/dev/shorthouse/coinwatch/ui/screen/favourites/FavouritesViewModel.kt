package dev.shorthouse.coinwatch.ui.screen.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.domain.GetFavouriteCoinsUseCaseOld
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavouriteCoinsUseCaseOld: GetFavouriteCoinsUseCaseOld
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavouritesUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        val favouriteCoinsFlow = getFavouriteCoinsUseCaseOld()

        favouriteCoinsFlow.onEach { favouriteCoinsResult ->
            when (favouriteCoinsResult) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            favouriteCoins = favouriteCoinsResult.data.toImmutableList(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = favouriteCoinsResult.message,
                            isLoading = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

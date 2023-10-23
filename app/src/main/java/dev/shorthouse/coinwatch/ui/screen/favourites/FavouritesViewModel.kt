package dev.shorthouse.coinwatch.ui.screen.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.domain.GetFavouriteCoinsUseCase
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavouriteCoinsUseCase: GetFavouriteCoinsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<FavouritesUiState>(FavouritesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        _uiState.update { FavouritesUiState.Loading }

        val favouriteCoinsFlow = getFavouriteCoinsUseCase()

        favouriteCoinsFlow.map { favouriteCoinsResult ->
            when (favouriteCoinsResult) {
                is Result.Error -> {
                    _uiState.update { FavouritesUiState.Error(favouriteCoinsResult.message) }
                }

                is Result.Success -> {
                    _uiState.update {
                        FavouritesUiState.Success(favouriteCoinsResult.data.toImmutableList())
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}

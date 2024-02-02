package dev.shorthouse.coinwatch.ui.screen.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.domain.GetFavouriteCoinIdsUseCase
import dev.shorthouse.coinwatch.domain.GetFavouriteCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.UpdateCachedFavouriteCoinsUseCase
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavouriteCoinsUseCase: GetFavouriteCoinsUseCase,
    private val getFavouriteCoinIdsUseCase: GetFavouriteCoinIdsUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val updateCachedFavouriteCoinsUseCase: UpdateCachedFavouriteCoinsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavouritesUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        val favouriteCoinIdsFlow = getFavouriteCoinIdsUseCase()
        val userPreferencesFlow = getUserPreferencesUseCase()

        combine(
            favouriteCoinIdsFlow,
            userPreferencesFlow
        ) { favouriteCoinIdsResult, userPreferences ->
            when (favouriteCoinIdsResult) {
                is Result.Success -> {
                    updateCachedFavouriteCoins(
                        coinIds = favouriteCoinIdsResult.data,
                        currency = userPreferences.currency,
                        coinSort = userPreferences.coinSort
                    )
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = favouriteCoinIdsResult.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)

        val favouriteCoinsFlow = getFavouriteCoinsUseCase()

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

    private suspend fun updateCachedFavouriteCoins(
        coinIds: List<FavouriteCoinId>,
        currency: Currency,
        coinSort: CoinSort
    ) {
        val updateCachedFavouriteCoinResult = updateCachedFavouriteCoinsUseCase(
            coinIds = coinIds,
            currency = currency,
            coinSort = coinSort
        )

        if (updateCachedFavouriteCoinResult is Result.Error) {
            _uiState.update {
                it.copy(
                    errorMessage = updateCachedFavouriteCoinResult.message
                )
            }
        }
    }
}

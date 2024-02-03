package dev.shorthouse.coinwatch.ui.screen.favourites

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.domain.GetFavouriteCoinIdsUseCase
import dev.shorthouse.coinwatch.domain.GetFavouriteCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.UpdateCachedFavouriteCoinsUseCase
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavouriteCoinsUseCase: GetFavouriteCoinsUseCase,
    private val updateCachedFavouriteCoinsUseCase: UpdateCachedFavouriteCoinsUseCase,
    private val getFavouriteCoinIdsUseCase: GetFavouriteCoinIdsUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavouritesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        _uiState.update { it.copy(isLoading = true) }

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
                    addErrorMessage(R.string.error_local_favourite_coin_ids)
                }
            }
        }.launchIn(viewModelScope)

        getFavouriteCoinsUseCase().onEach { favouriteCoinsResult ->
            when (favouriteCoinsResult) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            favouriteCoins = favouriteCoinsResult.data.toImmutableList(),
                        )
                    }
                }

                is Result.Error -> {
                    addErrorMessage(R.string.error_local_favourite_coins)
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }.launchIn(viewModelScope)
    }

    fun pullRefreshFavouriteCoins() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            delay(250.milliseconds)

            val favouriteCoinIdsResult = getFavouriteCoinIdsUseCase().first()
            val userPreferences = getUserPreferencesUseCase().first()

            when (favouriteCoinIdsResult) {
                is Result.Success -> {
                    updateCachedFavouriteCoins(
                        coinIds = favouriteCoinIdsResult.data,
                        currency = userPreferences.currency,
                        coinSort = userPreferences.coinSort
                    )
                }
                is Result.Error -> {
                    addErrorMessage(R.string.error_local_favourite_coin_ids)
                }
            }

            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun dismissErrorMessage(@StringRes dismissedErrorMessageId: Int) {
        _uiState.update {
            val errorMessageIds = it.errorMessageIds.filterNot { errorMessageId ->
                errorMessageId == dismissedErrorMessageId
            }
            it.copy(errorMessageIds = errorMessageIds)
        }
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
            addErrorMessage(R.string.error_network_favourite_coins)
        }
    }

    private fun addErrorMessage(@StringRes errorMessageId: Int) {
        _uiState.update {
            val errorMessages = it.errorMessageIds + errorMessageId
            it.copy(errorMessageIds = errorMessages)
        }
    }
}

package dev.shorthouse.coinwatch.ui.screen.favourites

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoinId
import dev.shorthouse.coinwatch.domain.favourites.GetFavouriteCoinIdsUseCase
import dev.shorthouse.coinwatch.domain.favourites.GetFavouriteCoinsUseCase
import dev.shorthouse.coinwatch.domain.preferences.GetFavouritesPreferencesUseCase
import dev.shorthouse.coinwatch.domain.preferences.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.favourites.UpdateCachedFavouriteCoinsUseCase
import dev.shorthouse.coinwatch.domain.preferences.UpdateFavouritesCoinSortUseCase
import dev.shorthouse.coinwatch.domain.preferences.UpdateIsFavouritesCondensedUseCase
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
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavouriteCoinsUseCase: GetFavouriteCoinsUseCase,
    private val updateCachedFavouriteCoinsUseCase: UpdateCachedFavouriteCoinsUseCase,
    private val getFavouriteCoinIdsUseCase: GetFavouriteCoinIdsUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val getFavouritesPreferencesUseCase: GetFavouritesPreferencesUseCase,
    private val updateIsFavouritesCondensedUseCase: UpdateIsFavouritesCondensedUseCase,
    private val updateFavouritesCoinSortUseCase: UpdateFavouritesCoinSortUseCase
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
        val favouritesPreferencesFlow = getFavouritesPreferencesUseCase()

        combine(
            favouriteCoinIdsFlow,
            userPreferencesFlow,
            favouritesPreferencesFlow
        ) { favouriteCoinIdsResult, userPreferences, favouritesPreferences ->
            when (favouriteCoinIdsResult) {
                is Result.Success -> {
                    updateCachedFavouriteCoins(
                        coinIds = favouriteCoinIdsResult.data,
                        currency = userPreferences.currency,
                        coinSort = favouritesPreferences.coinSort
                    )
                }

                is Result.Error -> {
                    addErrorMessage(R.string.error_local_favourite_coin_ids)
                }
            }
        }.launchIn(viewModelScope)

        getFavouritesPreferencesUseCase().onEach { favouritesPreferences ->
            _uiState.update {
                it.copy(
                    isFavouritesCondensed = favouritesPreferences.isFavouritesCondensed,
                    coinSort = favouritesPreferences.coinSort
                )
            }
        }.launchIn(viewModelScope)

        getFavouriteCoinsUseCase().onEach { favouriteCoinsResult ->
            when (favouriteCoinsResult) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            favouriteCoins = favouriteCoinsResult.data.toImmutableList(),
                            isFavouriteCoinsEmpty = favouriteCoinsResult.data.isEmpty()
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
            val favouritesPreferences = getFavouritesPreferencesUseCase().first()

            when (favouriteCoinIdsResult) {
                is Result.Success -> {
                    updateCachedFavouriteCoins(
                        coinIds = favouriteCoinIdsResult.data,
                        currency = userPreferences.currency,
                        coinSort = favouritesPreferences.coinSort
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

    fun updateIsFavouritesCondensed(isCondensed: Boolean) {
        viewModelScope.launch {
            updateIsFavouritesCondensedUseCase(isCondensed = isCondensed)
        }
    }

    fun updateCoinSort(coinSort: CoinSort) {
        viewModelScope.launch {
            updateFavouritesCoinSortUseCase(coinSort = coinSort)
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

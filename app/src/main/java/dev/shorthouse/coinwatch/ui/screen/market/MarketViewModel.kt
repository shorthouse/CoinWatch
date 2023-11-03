package dev.shorthouse.coinwatch.ui.screen.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.data.datastore.Currency
import dev.shorthouse.coinwatch.domain.GetCachedCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.RefreshCachedCoinsUseCase
import dev.shorthouse.coinwatch.domain.UpdateCoinSortUseCase
import dev.shorthouse.coinwatch.domain.UpdateCurrencyUseCase
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getCachedCoinsUseCase: GetCachedCoinsUseCase,
    private val refreshCachedCoinsUseCase: RefreshCachedCoinsUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val updateCoinSortUseCase: UpdateCoinSortUseCase,
    private val updateCurrencyUseCase: UpdateCurrencyUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MarketUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        refreshCachedCoins()

        getCachedCoinsUseCase().onEach { coinsResult ->
            _uiState.update { it.copy(isLoading = true) }

            when (coinsResult) {
                is Result.Success -> {
                    val coins = coinsResult.data.toImmutableList()

                    _uiState.update {
                        it.copy(
                            coins = coins,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = coinsResult.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)

        getUserPreferencesUseCase().onEach { userPreferences ->
            _uiState.update {
                it.copy(
                    coinSort = userPreferences.coinSort,
                    coinCurrency = userPreferences.currency
                )
            }

            refreshCachedCoins()
        }.launchIn(viewModelScope)
    }

    fun refreshCachedCoins() {
        viewModelScope.launch {
            refreshCachedCoinsUseCase()
        }
    }

    fun updateCoinSort(coinSort: CoinSort) {
        viewModelScope.launch {
            updateCoinSortUseCase(coinSort = coinSort)
        }
    }

    fun updateShowCoinSortBottomSheet(showSheet: Boolean) {
        _uiState.update { it.copy(showCoinSortBottomSheet = showSheet) }
    }

    fun updateCoinCurrency(currency: Currency) {
        viewModelScope.launch {
            updateCurrencyUseCase(currency = currency)
        }
    }

    fun updateShowCoinCurrencyBottomSheet(showSheet: Boolean) {
        _uiState.update { it.copy(showCoinCurrencyBottomSheet = showSheet) }
    }
}

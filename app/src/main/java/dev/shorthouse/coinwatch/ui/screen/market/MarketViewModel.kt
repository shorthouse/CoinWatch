package dev.shorthouse.coinwatch.ui.screen.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.data.datastore.Currency
import dev.shorthouse.coinwatch.domain.GetCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.UpdateCoinSortUseCase
import dev.shorthouse.coinwatch.domain.UpdateCurrencyUseCase
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val updateCoinSortUseCase: UpdateCoinSortUseCase,
    private val updateCurrencyUseCase: UpdateCurrencyUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<MarketUiState>(MarketUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
    }

    fun initialiseUiState() {
        _uiState.update { MarketUiState.Loading }

        val coinsFlow = getCoinsUseCase()
        val userPreferencesFlow = getUserPreferencesUseCase()

        combine(coinsFlow, userPreferencesFlow) { coinsResult, userPreferences ->
            when (coinsResult) {
                is Result.Error -> {
                    _uiState.update { MarketUiState.Error(coinsResult.message) }
                }

                is Result.Success -> {
                    val coins = coinsResult.data.toImmutableList()

                    _uiState.update {
                        if (it is MarketUiState.Success) {
                            it.copy(
                                coins = coins,
                                coinSort = userPreferences.coinSort,
                                coinCurrency = userPreferences.currency
                            )
                        } else {
                            MarketUiState.Success(
                                coins = coins,
                                coinSort = userPreferences.coinSort,
                                showCoinSortBottomSheet = false,
                                coinCurrency = userPreferences.currency,
                                showCoinCurrencyBottomSheet = false
                            )
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateCoinSort(coinSort: CoinSort) {
        viewModelScope.launch {
            updateCoinSortUseCase(coinSort = coinSort)
        }
    }

    fun updateShowCoinSortBottomSheet(showSheet: Boolean) {
        _uiState.update {
            if (it is MarketUiState.Success) {
                it.copy(showCoinSortBottomSheet = showSheet)
            } else {
                it
            }
        }
    }

    fun updateCoinCurrency(currency: Currency) {
        viewModelScope.launch {
            updateCurrencyUseCase(currency = currency)
        }
    }

    fun updateShowCoinCurrencyBottomSheet(showSheet: Boolean) {
        _uiState.update {
            if (it is MarketUiState.Success) {
                it.copy(showCoinCurrencyBottomSheet = showSheet)
            } else {
                it
            }
        }
    }
}

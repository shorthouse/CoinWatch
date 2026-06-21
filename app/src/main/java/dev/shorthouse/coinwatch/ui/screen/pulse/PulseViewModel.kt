package dev.shorthouse.coinwatch.ui.screen.pulse

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.R
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.domain.preferences.GetUserPreferencesUseCase
import dev.shorthouse.coinwatch.domain.pulse.GetFearGreedUseCase
import dev.shorthouse.coinwatch.domain.pulse.GetGlobalMarketUseCase
import dev.shorthouse.coinwatch.domain.pulse.GetMoversUseCase
import dev.shorthouse.coinwatch.domain.pulse.GetTrendingCoinsUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class PulseViewModel @Inject constructor(
    private val getFearGreedUseCase: GetFearGreedUseCase,
    private val getGlobalMarketUseCase: GetGlobalMarketUseCase,
    private val getTrendingCoinsUseCase: GetTrendingCoinsUseCase,
    private val getMoversUseCase: GetMoversUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PulseUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initialiseUiState()
        observeCurrencyChanges()
    }

    fun initialiseUiState() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currency = getUserPreferencesUseCase().first().currency
            loadFearGreed()
            loadGlobalMarket(currency = currency)
            loadTrendingCoins(currency = currency)
            loadMovers(currency = currency)

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun refreshUiState() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            delay(250.milliseconds)
            
            val currency = getUserPreferencesUseCase().first().currency
            loadFearGreed()
            loadGlobalMarket(currency = currency)
            loadTrendingCoins(currency = currency)
            loadMovers(currency = currency)

            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun dismissErrorMessage(@StringRes dismissedErrorMessageId: Int) {
        _uiState.update {
            it.copy(errorMessageIds = it.errorMessageIds - dismissedErrorMessageId)
        }
    }

    private suspend fun loadFearGreed() {
        when (val result = getFearGreedUseCase()) {
            is Result.Success -> {
                _uiState.update {
                    it.copy(fearGreed = result.data)
                }
            }

            is Result.Error -> {
                addErrorMessage(R.string.error_pulse_market_mood)
            }
        }
    }

    private suspend fun loadGlobalMarket(currency: Currency) {
        when (val result = getGlobalMarketUseCase(currency = currency)) {
            is Result.Success -> {
                _uiState.update {
                    it.copy(globalMarket = result.data)
                }
            }

            is Result.Error -> {
                addErrorMessage(R.string.error_pulse_global_market)
            }
        }
    }

    private suspend fun loadTrendingCoins(currency: Currency) {
        when (val result = getTrendingCoinsUseCase(currency = currency)) {
            is Result.Success -> {
                _uiState.update {
                    it.copy(trendingCoins = result.data.toPersistentList())
                }
            }

            is Result.Error -> {
                addErrorMessage(R.string.error_pulse_trending)
            }
        }
    }

    private suspend fun loadMovers(currency: Currency) {
        when (val result = getMoversUseCase(currency = currency)) {
            is Result.Success -> {
                _uiState.update {
                    it.copy(movers = result.data)
                }
            }

            is Result.Error -> {
                addErrorMessage(R.string.error_pulse_movers)
            }
        }
    }

    private fun addErrorMessage(@StringRes errorMessageId: Int) {
        _uiState.update {
            it.copy(errorMessageIds = it.errorMessageIds + errorMessageId)
        }
    }

    private fun observeCurrencyChanges() {
        getUserPreferencesUseCase()
            .map { it.currency }
            .distinctUntilChanged()
            .drop(1)
            .onEach { currency ->
                loadGlobalMarket(currency = currency)
                loadTrendingCoins(currency = currency)
                loadMovers(currency = currency)
            }
            .launchIn(viewModelScope)
    }
}

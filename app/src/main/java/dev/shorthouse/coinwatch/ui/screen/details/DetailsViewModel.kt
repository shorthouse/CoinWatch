package dev.shorthouse.coinwatch.ui.screen.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.common.Constants.PARAM_COIN_ID
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoinId
import dev.shorthouse.coinwatch.domain.details.GetCoinChartUseCase
import dev.shorthouse.coinwatch.domain.details.GetCoinDetailsUseCase
import dev.shorthouse.coinwatch.domain.favourites.IsCoinFavouriteUseCase
import dev.shorthouse.coinwatch.domain.favourites.ToggleIsCoinFavouriteUseCase
import dev.shorthouse.coinwatch.domain.reviewprompt.RecordFavouriteAddedUseCase
import dev.shorthouse.coinwatch.domain.reviewprompt.RecordSuccessfulDetailsViewUseCase
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val getCoinChartUseCase: GetCoinChartUseCase,
    private val isCoinFavouriteUseCase: IsCoinFavouriteUseCase,
    private val toggleIsCoinFavouriteUseCase: ToggleIsCoinFavouriteUseCase,
    private val recordSuccessfulDetailsViewUseCase: RecordSuccessfulDetailsViewUseCase,
    private val recordFavouriteAddedUseCase: RecordFavouriteAddedUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val coinId = savedStateHandle.get<String>(PARAM_COIN_ID)
    private val chartPeriodFlow = MutableStateFlow(ChartPeriod.Day)
    private var hasRecordedSuccessfulDetailsView = false

    init {
        initialiseUiState()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun initialiseUiState() {
        if (coinId == null) {
            _uiState.update { DetailsUiState.Error("Invalid coin ID") }
            return
        }

        val coinDetailsFlow = getCoinDetailsUseCase(coinId = coinId)
        val coinChartFlow = chartPeriodFlow.flatMapLatest { chartPeriod ->
            getCoinChartUseCase(
                coinId = coinId,
                chartPeriod = chartPeriod.stringName
            )
        }
        val isCoinFavouriteFlow = isCoinFavouriteUseCase(
            favouriteCoinId = FavouriteCoinId(id = coinId)
        )

        combine(
            coinDetailsFlow,
            coinChartFlow,
            isCoinFavouriteFlow
        ) { coinDetailsResult, coinChartResult, isCoinFavouriteResult ->
            when {
                coinDetailsResult is Result.Error -> {
                    _uiState.update { DetailsUiState.Error(coinDetailsResult.message) }
                }

                coinChartResult is Result.Error -> {
                    _uiState.update { DetailsUiState.Error(coinChartResult.message) }
                }

                isCoinFavouriteResult is Result.Error -> {
                    _uiState.update { DetailsUiState.Error(isCoinFavouriteResult.message) }
                }

                coinDetailsResult is Result.Success &&
                    coinChartResult is Result.Success &&
                    isCoinFavouriteResult is Result.Success -> {
                    _uiState.update {
                        DetailsUiState.Success(
                            coinDetails = coinDetailsResult.data,
                            coinChart = coinChartResult.data,
                            chartPeriod = chartPeriodFlow.value,
                            isCoinFavourite = isCoinFavouriteResult.data
                        )
                    }

                    if (!hasRecordedSuccessfulDetailsView) {
                        runCatching {
                            recordSuccessfulDetailsViewUseCase()
                        }.onSuccess {
                            hasRecordedSuccessfulDetailsView = true
                        }.onFailure { throwable ->
                            if (throwable is CancellationException) throw throwable
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateChartPeriod(chartPeriod: ChartPeriod) {
        chartPeriodFlow.value = chartPeriod
    }

    fun toggleIsCoinFavourite() {
        if (coinId == null) return

        viewModelScope.launch {
            val favouriteCoinId = FavouriteCoinId(id = coinId)
            val isCoinFavouriteAfterToggle = toggleIsCoinFavouriteUseCase(
                favouriteCoinId = favouriteCoinId
            )

            if (isCoinFavouriteAfterToggle) {
                recordFavouriteAddedUseCase()
            }
        }
    }
}

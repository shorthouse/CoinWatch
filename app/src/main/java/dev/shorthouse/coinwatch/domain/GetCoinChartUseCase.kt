package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.userPreferences.UserPreferencesRepository
import dev.shorthouse.coinwatch.data.repository.chart.CoinChartRepository
import dev.shorthouse.coinwatch.model.CoinChart
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class GetCoinChartUseCase @Inject constructor(
    private val coinChartRepository: CoinChartRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(coinId: String, chartPeriod: String): Flow<Result<CoinChart>> {
        return getCoinChart(coinId = coinId, chartPeriod = chartPeriod)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCoinChart(coinId: String, chartPeriod: String): Flow<Result<CoinChart>> {
        return userPreferencesRepository.userPreferencesFlow.flatMapLatest { userPreferences ->
            coinChartRepository.getCoinChart(
                coinId = coinId,
                chartPeriod = chartPeriod,
                currency = userPreferences.currency
            )
        }
    }
}

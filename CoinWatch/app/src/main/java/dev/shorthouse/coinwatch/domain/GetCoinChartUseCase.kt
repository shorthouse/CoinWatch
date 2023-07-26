package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.chart.CoinChartRepository
import dev.shorthouse.coinwatch.model.CoinChart
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinChartUseCase @Inject constructor(
    private val coinChartRepository: CoinChartRepository
) {
    operator fun invoke(coinId: String, chartPeriodDays: String): Flow<Result<CoinChart>> {
        return getCoinChart(coinId = coinId, chartPeriodDays = chartPeriodDays)
    }

    private fun getCoinChart(coinId: String, chartPeriodDays: String): Flow<Result<CoinChart>> {
        return coinChartRepository.getCoinChart(coinId = coinId, chartPeriodDays = chartPeriodDays)
    }
}

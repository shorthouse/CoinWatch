package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.chart.CoinChartRepository
import dev.shorthouse.coinwatch.model.CoinChart
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCoinChartUseCase @Inject constructor(
    private val coinChartRepository: CoinChartRepository
) {
    operator fun invoke(coinId: String, chartPeriod: String): Flow<Result<CoinChart>> {
        return getCoinChart(coinId = coinId, chartPeriod = chartPeriod)
    }

    private fun getCoinChart(coinId: String, chartPeriod: String): Flow<Result<CoinChart>> {
        return coinChartRepository.getCoinChart(coinId = coinId, chartPeriod = chartPeriod)
    }
}

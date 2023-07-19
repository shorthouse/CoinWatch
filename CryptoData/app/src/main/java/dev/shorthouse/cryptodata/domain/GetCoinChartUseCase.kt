package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.repository.chart.CoinChartRepository
import dev.shorthouse.cryptodata.model.CoinChart
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

package dev.shorthouse.cryptodata.data.repository.chart

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.model.CoinChart
import kotlinx.coroutines.flow.Flow

interface CoinChartRepository {
    fun getCoinChart(coinId: String, chartPeriodDays: String): Flow<Result<CoinChart>>
}

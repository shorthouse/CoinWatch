package dev.shorthouse.coinwatch.data.repository.chart

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.model.CoinChart
import kotlinx.coroutines.flow.Flow

interface CoinChartRepository {
    fun getCoinChart(
        coinId: String,
        chartPeriod: String,
        currency: Currency
    ): Flow<Result<CoinChart>>
}

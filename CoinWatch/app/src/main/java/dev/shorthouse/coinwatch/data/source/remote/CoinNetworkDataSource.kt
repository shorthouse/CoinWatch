package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.remote.model.CoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import retrofit2.Response
import javax.inject.Inject

class CoinNetworkDataSource @Inject constructor(private val coinApi: CoinApi) {
    suspend fun getCoins(): Response<List<CoinApiModel>> {
        return coinApi.getCoins()
    }

    suspend fun getCoinDetail(coinId: String): Response<List<CoinDetailApiModel>> {
        return coinApi.getCoinDetail(coinId = coinId)
    }

    suspend fun getCoinChart(
        coinId: String,
        chartPeriodDays: String
    ): Response<CoinChartApiModel> {
        return coinApi.getCoinChart(coinId = coinId, periodDays = chartPeriodDays)
    }

    suspend fun getMarketStats(): Response<MarketStatsApiModel> {
        return coinApi.getMarketStats()
    }
}

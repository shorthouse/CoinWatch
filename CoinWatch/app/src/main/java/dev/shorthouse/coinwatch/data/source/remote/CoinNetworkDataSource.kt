package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import javax.inject.Inject
import retrofit2.Response

class CoinNetworkDataSource @Inject constructor(private val coinApi: CoinApi) {
    suspend fun getCoins(): Response<CoinsApiModel> {
        return coinApi.getCoins()
    }

    suspend fun getCoinDetail(coinId: String): Response<CoinDetailApiModel> {
        return coinApi.getCoinDetail(coinId = coinId)
    }

    suspend fun getCoinChart(
        coinId: String,
        chartPeriod: String
    ): Response<CoinChartApiModel> {
        return coinApi.getCoinChart(coinId = coinId, chartPeriod = chartPeriod)
    }

    suspend fun getMarketStats(): Response<MarketStatsApiModel> {
        return coinApi.getMarketStats()
    }
}

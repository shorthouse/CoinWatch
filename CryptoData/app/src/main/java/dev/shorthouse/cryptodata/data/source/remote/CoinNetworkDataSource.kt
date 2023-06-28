package dev.shorthouse.cryptodata.data.source.remote

import dev.shorthouse.cryptodata.data.source.remote.model.CoinApiModel
import dev.shorthouse.cryptodata.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.cryptodata.data.source.remote.model.CoinDetailApiModel
import javax.inject.Inject
import retrofit2.Response

class CoinNetworkDataSource @Inject constructor(
    private val coinApi: CoinApi
) {
    suspend fun getCoins(): Response<List<CoinApiModel>> {
        return coinApi.getCoins()
    }

    suspend fun getCoinDetail(coinId: String): Response<CoinDetailApiModel> {
        return coinApi.getCoinDetail(coinId = coinId)
    }

    suspend fun getCoinChart(
        coinId: String,
        chartPeriodDays: String
    ): Response<CoinChartApiModel> {
        return coinApi.getCoinChart(coinId = coinId, periodDays = chartPeriodDays)
    }
}

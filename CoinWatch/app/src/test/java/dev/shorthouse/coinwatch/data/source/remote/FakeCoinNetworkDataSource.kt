package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import retrofit2.Response

class FakeCoinNetworkDataSource(
    private val coinApi: CoinApi
) : CoinNetworkDataSource {
    override suspend fun getCoins(currencyUUID: String): Response<CoinsApiModel> {
        return coinApi.getCoins(currencyUUID = currencyUUID)
    }

    override suspend fun getCoinDetail(coinId: String): Response<CoinDetailApiModel> {
        return coinApi.getCoinDetail(coinId = coinId)
    }

    override suspend fun getCoinChart(
        coinId: String,
        chartPeriod: String
    ): Response<CoinChartApiModel> {
        return coinApi.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod
        )
    }
}

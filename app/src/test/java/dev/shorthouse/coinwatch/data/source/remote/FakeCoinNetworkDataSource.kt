package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import retrofit2.Response

class FakeCoinNetworkDataSource(
    private val coinApi: CoinApi
) : CoinNetworkDataSource {
    override suspend fun getCoins(coinIds: List<String>): Response<CoinsApiModel> {
        return coinApi.getCoins(coinIds = coinIds)
    }

    override suspend fun getCoinDetails(coinId: String): Response<CoinDetailsApiModel> {
        return coinApi.getCoinDetails(coinId = coinId)
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

    override suspend fun getCoinSearchResults(
        searchQuery: String
    ): Response<CoinSearchResultsApiModel> {
        return coinApi.getCoinSearchResults(searchQuery = searchQuery)
    }
}

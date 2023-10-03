package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import retrofit2.Response

interface CoinNetworkDataSource {
    suspend fun getCoins(coinIds: List<String>): Response<CoinsApiModel>

    suspend fun getCoinDetail(coinId: String): Response<CoinDetailApiModel>

    suspend fun getCoinChart(
        coinId: String,
        chartPeriod: String
    ): Response<CoinChartApiModel>

    suspend fun getCoinSearchResults(searchQuery: String): Response<CoinSearchResultsApiModel>
}

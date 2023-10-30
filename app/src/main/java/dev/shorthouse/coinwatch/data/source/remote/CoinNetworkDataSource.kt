package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import retrofit2.Response

interface CoinNetworkDataSource {
    suspend fun getCoins(coinIds: List<String>, coinSort: CoinSort): Response<CoinsApiModel>

    suspend fun getCoinDetails(coinId: String): Response<CoinDetailsApiModel>

    suspend fun getCoinChart(
        coinId: String,
        chartPeriod: String
    ): Response<CoinChartApiModel>

    suspend fun getCoinSearchResults(searchQuery: String): Response<CoinSearchResultsApiModel>
}

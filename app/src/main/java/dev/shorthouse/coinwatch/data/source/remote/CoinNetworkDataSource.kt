package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import retrofit2.Response

interface CoinNetworkDataSource {
    suspend fun getCoins(
        coinIds: List<String>,
        coinSort: CoinSort,
        currency: Currency
    ): Response<CoinsApiModel>

    suspend fun getCoinDetails(
        coinId: String,
        currency: Currency
    ): Response<CoinDetailsApiModel>

    suspend fun getCoinChart(
        coinId: String,
        chartPeriod: String,
        currency: Currency
    ): Response<CoinChartApiModel>

    suspend fun getCoinSearchResults(searchQuery: String): Response<CoinSearchResultsApiModel>
}

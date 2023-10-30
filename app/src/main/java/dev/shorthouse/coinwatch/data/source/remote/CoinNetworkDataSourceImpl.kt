package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import javax.inject.Inject
import retrofit2.Response

class CoinNetworkDataSourceImpl @Inject constructor(
    private val coinApi: CoinApi
) : CoinNetworkDataSource {
    override suspend fun getCoins(
        coinIds: List<String>,
        coinSort: CoinSort
    ): Response<CoinsApiModel> {
        val orderBy = when (coinSort) {
            CoinSort.MarketCap -> "marketCap"
            CoinSort.Price -> "price"
            CoinSort.PriceChange24h -> "change"
            CoinSort.Volume24h -> "24hVolume"
        }

        return coinApi.getCoins(coinIds = coinIds, orderBy = orderBy)
    }

    override suspend fun getCoinDetails(coinId: String): Response<CoinDetailsApiModel> {
        return coinApi.getCoinDetails(coinId = coinId)
    }

    override suspend fun getCoinChart(
        coinId: String,
        chartPeriod: String
    ): Response<CoinChartApiModel> {
        return coinApi.getCoinChart(coinId = coinId, chartPeriod = chartPeriod)
    }

    override suspend fun getCoinSearchResults(
        searchQuery: String
    ): Response<CoinSearchResultsApiModel> {
        return coinApi.getCoinSearchResults(searchQuery = searchQuery)
    }
}

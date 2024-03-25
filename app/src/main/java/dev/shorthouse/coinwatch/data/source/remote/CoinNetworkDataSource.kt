package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.preferences.global.CoinSort
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.data.preferences.market.MarketCoinSort
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import retrofit2.Response

interface CoinNetworkDataSource {
    suspend fun getCoins(
        marketCoinSort: MarketCoinSort,
        currency: Currency
    ): Response<CoinsApiModel>

    suspend fun getFavouriteCoins(
        coinIds: List<String>,
        coinSort: CoinSort,
        currency: Currency
    ): Response<FavouriteCoinsApiModel>

    suspend fun getCoinDetails(coinId: String, currency: Currency): Response<CoinDetailsApiModel>

    suspend fun getCoinChart(
        coinId: String,
        chartPeriod: String,
        currency: Currency
    ): Response<CoinChartApiModel>

    suspend fun getCoinSearchResults(searchQuery: String): Response<CoinSearchResultsApiModel>

    suspend fun getMarketStats(): Response<MarketStatsApiModel>
}

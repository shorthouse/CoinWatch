package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApi {
    @GET("coins")
    suspend fun getCoins(
        @Query("uuids[]") coinIds: List<String> = emptyList(),
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl",
        @Query("orderBy") orderBy: String = "marketCap",
        @Query("timePeriod") timePeriod: String = "24h",
        @Query("orderDirection") orderDirection: String = "desc",
        @Query("limit") limit: String = "100"
    ): Response<CoinsApiModel>

    @GET("coins")
    suspend fun getFavouriteCoins(
        @Query("uuids[]") coinIds: List<String> = emptyList(),
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl",
        @Query("orderBy") orderBy: String = "marketCap",
        @Query("timePeriod") timePeriod: String = "24h",
        @Query("orderDirection") orderDirection: String = "desc",
        @Query("limit") limit: String = "100"
    ): Response<FavouriteCoinsApiModel>

    @GET("coin/{coinId}")
    suspend fun getCoinDetails(
        @Path("coinId") coinId: String,
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl"
    ): Response<CoinDetailsApiModel>

    @GET("coin/{coinId}/history")
    suspend fun getCoinChart(
        @Path("coinId") coinId: String,
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl",
        @Query("timePeriod") chartPeriod: String = "24h"
    ): Response<CoinChartApiModel>

    @GET("search-suggestions")
    suspend fun getCoinSearchResults(
        @Query("query") searchQuery: String = "",
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl"
    ): Response<CoinSearchResultsApiModel>

    @GET("stats/coins")
    suspend fun getMarketStats(): Response<MarketStatsApiModel>
}

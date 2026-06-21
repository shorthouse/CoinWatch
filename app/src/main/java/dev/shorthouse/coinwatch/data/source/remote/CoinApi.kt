package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FearGreedApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MoversApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.TrendingCoinsApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApi {
    @GET("coins")
    suspend fun getCoins(
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl",
        @Query("orderBy") orderBy: String = "marketCap",
        @Query("timePeriod") timePeriod: String = "24h",
        @Query("orderDirection") orderDirection: String = "desc",
        @Query("limit") limit: String = "100"
    ): Response<CoinsApiModel>

    @GET("coins")
    suspend fun getFavouriteCoins(
        @Query("uuids[]") coinIds: List<String>,
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

    @GET("coin/{coinId}/price-history")
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

    @GET("stats")
    suspend fun getGlobalStats(
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl"
    ): Response<GlobalStatsApiModel>

    @GET("stats/coins")
    suspend fun getGlobalMarketCoinStats(
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl",
        @Query("timePeriod") timePeriod: String = "24h",
    ): Response<GlobalMarketCoinStatsApiModel>

    @GET("indicators/fear-and-greed")
    suspend fun getFearGreed(
        @Query("timePeriod") timePeriod: String = "30d",
        @Query("interval") interval: String = "day",
    ): Response<FearGreedApiModel>

    @GET("coins/trending")
    suspend fun getTrendingCoins(
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl",
        @Query("timePeriod") timePeriod: String = "24h",
        @Query("tiers[]") tiers: List<Int> = listOf(1, 2),
    ): Response<TrendingCoinsApiModel>

    @GET("coins")
    suspend fun getMovers(
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl",
        @Query("orderBy") orderBy: String = "change",
        @Query("orderDirection") orderDirection: String = "desc",
        @Query("timePeriod") timePeriod: String = "24h",
        @Query("limit") limit: String = "100"
    ): Response<MoversApiModel>
}

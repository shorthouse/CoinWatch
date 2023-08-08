package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApi {
    @GET("coins")
    suspend fun getCoins(
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl",
        @Query("timePeriod") timePeriod: String = "24h",
        @Query("orderBy") orderBy: String = "marketCap",
        @Query("orderDirection") orderDirection: String = "desc",
        @Query("limit") limit: String = "50"
    ): Response<CoinsApiModel>

    @GET("coin/{coinId}")
    suspend fun getCoinDetail(
        @Path("coinId") coinId: String,
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl"
    ): Response<CoinDetailApiModel>

    @GET("coin/{coinId}/history")
    suspend fun getCoinChart(
        @Path("coinId") coinId: String,
        @Query("referenceCurrencyUuid") currencyUUID: String = "yhjMzLPhuIDl",
        @Query("timePeriod") chartPeriod: String = "24h"
    ): Response<CoinChartApiModel>

//    @GET("coins/markets")
//    suspend fun getCoins(
//        @Query("vs_currency") currency: String = "usd",
//        @Query("order") order: String = "market_cap_desc",
//        @Query("per_page") coinsPerPage: Int = 30,
//        @Query("page") page: Int = 1,
//        @Query("sparkline") includeSparkline7dData: Boolean = true,
//        @Query("price_change_percentage") priceChangePercentagePeriods: String = "24h",
//        @Query("locale") locale: String = "en",
//        @Query("precision") currencyDecimalPlaces: String = "18",
//    ): Response<List<CoinApiModel>>

//    @GET("coins/markets")
//    suspend fun getCoinDetail(
//        @Query("ids") coinId: String,
//        @Query("vs_currency") currency: String = "usd",
//        @Query("order") order: String = "market_cap_desc",
//        @Query("per_page") coinsPerPage: Int = 100,
//        @Query("page") page: Int = 1,
//        @Query("sparkline") includeSparklineData: Boolean = false,
//        @Query("price_change_percentage") priceChangePercentagePeriods: String = "24h",
//        @Query("locale") locale: String = "en",
//        @Query("precision") currencyDecimalPlaces: String = "18",
//    ): Response<List<CoinDetailApiModel>>

//    @GET("coins/{coinId}/market_chart")
//    suspend fun getCoinChart(
//        @Path("coinId") coinId: String,
//        @Query("days") periodDays: String = "1",
//        @Query("vs_currency") currency: String = "usd",
//        @Query("precision") currencyDecimalPlaces: String = "18"
//    ): Response<CoinChartApiModel>

    @GET("global")
    suspend fun getMarketStats(): Response<MarketStatsApiModel>
}

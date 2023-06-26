package dev.shorthouse.cryptodata.data.source.remote

import dev.shorthouse.cryptodata.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.cryptodata.data.source.remote.model.CoinListItemApiModel
import dev.shorthouse.cryptodata.data.source.remote.model.CoinPastPricesApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApi {
    @GET("coins/markets")
    suspend fun getCoinListItems(
        @Query("vs_currency") currency: String = "usd",
        @Query("price_change_percentage") priceChangePercentage: String = "24h",
        @Query("precision") currentPricePrecision: String = "2",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") coinsPerPage: Int = 100,
        @Query("page") pageNum: Int = 1,
        @Query("sparkline") sparkline: Boolean = false,
        @Query("locale") locale: String = "en"
    ): Response<List<CoinListItemApiModel>>

    @GET("coins/{coinId}")
    suspend fun getCoinDetail(
        @Path("coinId") coinId: String,
        @Query("localization") localization: Boolean = false,
        @Query("tickers") tickets: Boolean = false,
        @Query("community_data") communityData: Boolean = false,
        @Query("developer_data") developerData: Boolean = false,
        @Query("sparkline") sparkline: Boolean = true
    ): Response<CoinDetailApiModel>

    @GET("coins/{coinId}/market_chart")
    suspend fun getCoinPastPrices(
        @Path("coinId") coinId: String,
        @Query("days") periodDays: String = "7",
        @Query("vs_currency") currency: String = "usd",
        @Query("precision") numDecimalPlaces: String = "2"
    ): Response<CoinPastPricesApiModel>
}

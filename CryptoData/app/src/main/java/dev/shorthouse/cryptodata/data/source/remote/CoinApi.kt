package dev.shorthouse.cryptodata.data.source.remote

import dev.shorthouse.cryptodata.data.source.remote.dto.CoinDetailDto
import dev.shorthouse.cryptodata.data.source.remote.dto.CoinDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApi {
    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String = "usd",
        @Query("price_change_percentage") priceChangePercentage: String = "24h",
        @Query("precision") currentPricePrecision: String = "2",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") coinsPerPage: Int = 100,
        @Query("page") pageNum: Int = 1,
        @Query("sparkline") sparkline: Boolean = false,
        @Query("locale") locale: String = "en"
    ): Response<List<CoinDto>>

    @GET("coins/{coinId}")
    suspend fun getCoinDetail(
        @Path("coinId") coinId: String,
        @Query("localization") localization: Boolean = false,
        @Query("tickers") tickets: Boolean = false,
        @Query("community_data") communityData: Boolean = false,
        @Query("developer_data") developerData: Boolean = false,
        @Query("sparkline") sparkline: Boolean = true
    ): Response<CoinDetailDto>
}

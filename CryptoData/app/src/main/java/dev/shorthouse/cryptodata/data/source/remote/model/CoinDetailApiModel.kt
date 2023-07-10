package dev.shorthouse.cryptodata.data.source.remote.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CoinDetailApiModel(
    val id: String,
    val name: String?,
    val symbol: String?,
    val image: String?,
    @SerializedName("current_price")
    val currentPrice: BigDecimal?,
    @SerializedName("market_cap")
    val marketCap: BigDecimal?,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int?,
    @SerializedName("circulating_supply")
    val circulatingSupply: BigDecimal?,
    @SerializedName("atl")
    val allTimeLow: BigDecimal?,
    @SerializedName("atl_date")
    val allTimeLowDate: String?,
    @SerializedName("ath")
    val allTimeHigh: BigDecimal?,
    @SerializedName("ath_date")
    val allTimeHighDate: String?
)

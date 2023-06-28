package dev.shorthouse.cryptodata.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class CoinDetailApiModel(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("market_cap")
    val marketCap: Double,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double,
    @SerializedName("atl")
    val allTimeLow: Double,
    @SerializedName("atl_date")
    val allTimeLowDate: String,
    @SerializedName("ath")
    val allTimeHigh: Double,
    @SerializedName("ath_date")
    val allTimeHighDate: String
)

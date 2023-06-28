package dev.shorthouse.cryptodata.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class CoinDetailApiModel(
    val id: String,
    val name: String,
    val symbol: String,
    val image: Image,
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("market_data")
    val marketData: MarketData
)

data class Image(
    val large: String
)

data class CurrentPrice(
    val usd: Double
)

data class AllTimeHigh(
    val usd: Double
)

data class AllTimeHighDate(
    val usd: String
)

data class AllTimeLow(
    val usd: Double
)

data class AllTimeLowDate(
    val usd: String
)

data class MarketCap(
    val usd: Long
)

data class MarketData(
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("market_cap")
    val marketCap: MarketCap,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double,
    @SerializedName("atl")
    val allTimeLow: AllTimeLow,
    @SerializedName("ath")
    val allTimeHigh: AllTimeHigh,
    @SerializedName("atl_date")
    val allTimeLowDate: AllTimeLowDate,
    @SerializedName("ath_date")
    val allTimeHighDate: AllTimeHighDate
)

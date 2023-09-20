package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class CoinDetailApiModel(
    @SerializedName("data")
    val coinDetailData: CoinDetailData
)

data class CoinDetailData(
    @SerializedName("coin")
    val coinDetail: CoinDetail
)

data class CoinDetail(
    @SerializedName("uuid")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("iconUrl")
    val iconUrl: String?,
    @SerializedName("price")
    val currentPrice: String?,
    @SerializedName("marketCap")
    val marketCap: String?,
    @SerializedName("rank")
    val marketCapRank: String?,
    @SerializedName("24hVolume")
    val volume24h: String?,
    @SerializedName("supply")
    val supply: Supply?,
    @SerializedName("allTimeHigh")
    val allTimeHigh: AllTimeHigh?,
    @SerializedName("listedAt")
    val listedAt: Long?
)

data class Supply(
    @SerializedName("circulating")
    val circulatingSupply: String?
)

data class AllTimeHigh(
    @SerializedName("price")
    val price: String?,
    @SerializedName("timestamp")
    val timestamp: Long?
)

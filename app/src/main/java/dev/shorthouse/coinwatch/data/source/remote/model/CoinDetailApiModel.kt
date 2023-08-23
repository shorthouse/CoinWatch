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
    val name: String?,
    val symbol: String?,
    val iconUrl: String?,
    @SerializedName("price")
    val currentPrice: String?,
    val marketCap: String?,
    @SerializedName("rank")
    val marketCapRank: String?,
    @SerializedName("24hVolume")
    val volume24h: String?,
    val supply: Supply?,
    val allTimeHigh: AllTimeHigh?,
    val listedAt: Long?
)

data class Supply(
    @SerializedName("circulating")
    val circulatingSupply: String?
)

data class AllTimeHigh(
    val price: String?,
    val timestamp: Long?
)

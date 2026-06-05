package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class GlobalStatsApiModel(
    @SerializedName("data")
    val data: GlobalStatsData?
)

data class GlobalStatsData(
    @SerializedName("totalMarketCap")
    val totalMarketCap: String?,
    @SerializedName("total24hVolume")
    val total24hVolume: String?,
    @SerializedName("btcDominance")
    val btcDominance: Double?,
)

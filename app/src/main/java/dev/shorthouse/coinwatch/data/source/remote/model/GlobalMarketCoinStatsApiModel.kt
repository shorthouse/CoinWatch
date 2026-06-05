package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class GlobalMarketCoinStatsApiModel(
    @SerializedName("data")
    val data: GlobalMarketCoinStatsDataHolder?
)

data class GlobalMarketCoinStatsDataHolder(
    @SerializedName("stats")
    val stats: GlobalMarketCoinStatsData?
)

data class GlobalMarketCoinStatsData(
    @SerializedName("positiveChanges")
    val positiveChanges: Int?,
    @SerializedName("negativeChanges")
    val negativeChanges: Int?,
)

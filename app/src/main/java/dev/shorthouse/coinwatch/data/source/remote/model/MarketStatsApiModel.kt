package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class MarketStatsApiModel(
    @SerializedName("data")
    val marketStatsDataHolder: MarketStatsDataHolder?
)

data class MarketStatsDataHolder(
    @SerializedName("stats")
    val marketStatsData: MarketStatsData?
)

data class MarketStatsData(
    @SerializedName("marketCapChange")
    val marketCapChangePercentage24h: String?
)

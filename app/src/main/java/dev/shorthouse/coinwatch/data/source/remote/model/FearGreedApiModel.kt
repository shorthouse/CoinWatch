package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class FearGreedApiModel(
    @SerializedName("data")
    val data: List<FearGreedDataPointApiModel?>?
)

data class FearGreedDataPointApiModel(
    @SerializedName("timestamp")
    val timestamp: Long?,
    @SerializedName("value")
    val value: Double?,
)

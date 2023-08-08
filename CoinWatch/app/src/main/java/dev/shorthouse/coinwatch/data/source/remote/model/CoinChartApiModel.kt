package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class CoinChartApiModel(
    @SerializedName("data")
    val coinChartData: CoinChartData
)

data class CoinChartData(
    @SerializedName("change")
    val pricePercentageChange: String,
    val history: List<History>
)

data class History(
    val price: String?
)

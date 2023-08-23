package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class CoinChartApiModel(
    @SerializedName("data")
    val coinChartData: CoinChartData
)

data class CoinChartData(
    @SerializedName("change")
    val pricePercentageChange: String?,
    @SerializedName("history")
    val pastPrices: List<PastPrice?>?
)

data class PastPrice(
    @SerializedName("price")
    val amount: String?
)

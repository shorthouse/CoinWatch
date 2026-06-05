package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class TrendingCoinsApiModel(
    @SerializedName("data")
    val trendingCoinsData: TrendingCoinsData?
)

data class TrendingCoinsData(
    @SerializedName("coins")
    val coins: List<TrendingCoinApiModel?>?
)

data class TrendingCoinApiModel(
    @SerializedName("uuid")
    val id: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("iconUrl")
    val imageUrl: String?,
    @SerializedName("price")
    val currentPrice: String?,
    @SerializedName("change")
    val priceChangePercentage24h: String?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("sparkline")
    val sparkline: List<String?>?
)

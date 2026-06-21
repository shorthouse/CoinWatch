package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class MoversApiModel(
    @SerializedName("data")
    val moversData: MoversData?
)

data class MoversData(
    @SerializedName("coins")
    val coins: List<MoverCoinApiModel?>?
)

data class MoverCoinApiModel(
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
    @SerializedName("sparkline")
    val sparkline: List<String?>?
)

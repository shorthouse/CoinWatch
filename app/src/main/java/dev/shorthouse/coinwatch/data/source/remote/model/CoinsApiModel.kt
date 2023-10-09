package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CoinsApiModel(
    @SerializedName("data")
    val coinsData: CoinsData?
)

data class CoinsData(
    @SerializedName("coins")
    val coins: List<CoinApiModel?>?
)

data class CoinApiModel(
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
    val prices24h: List<BigDecimal?>?
)

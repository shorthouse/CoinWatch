package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CoinsApiModel(
    @SerializedName("data")
    val coinsData: CoinsData
)

data class CoinsData(
    val coins: List<Coin?>?
)

data class Coin(
    @SerializedName("uuid")
    val id: String?,
    val symbol: String?,
    val name: String?,
    val iconUrl: String?,
    @SerializedName("price")
    val currentPrice: String?,
    @SerializedName("change")
    val priceChangePercentage24h: String?,
    @SerializedName("sparkline")
    val sparkline24h: List<BigDecimal>?
)

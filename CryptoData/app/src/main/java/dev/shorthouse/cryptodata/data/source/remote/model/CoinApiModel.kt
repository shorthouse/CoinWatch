package dev.shorthouse.cryptodata.data.source.remote.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CoinApiModel(
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    @SerializedName("current_price")
    val currentPrice: BigDecimal,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int
)

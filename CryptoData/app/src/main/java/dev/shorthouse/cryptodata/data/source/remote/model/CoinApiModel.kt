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
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: BigDecimal,
    @SerializedName("sparkline_in_7d")
    val sparkline7d: Sparkline7d
)

data class Sparkline7d(
    @SerializedName("price")
    val prices: List<BigDecimal>
)

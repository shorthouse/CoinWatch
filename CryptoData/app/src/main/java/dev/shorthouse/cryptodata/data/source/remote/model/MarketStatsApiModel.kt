package dev.shorthouse.cryptodata.data.source.remote.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class MarketStatsApiModel(
    @SerializedName("data")
    val globalMarketData: GlobalMarketData
)

data class GlobalMarketData(
    @SerializedName("market_cap_change_percentage_24h_usd")
    val marketCapChangePercentage24h: BigDecimal,
)

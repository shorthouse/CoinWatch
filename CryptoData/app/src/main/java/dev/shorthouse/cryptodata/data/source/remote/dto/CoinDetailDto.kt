package dev.shorthouse.cryptodata.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class CoinDetailDto(
    val id: String,
    val name: String,
    val symbol: String,
    val image: Image,
    @SerializedName("genesis_date")
    val genesisDate: String,
    @SerializedName("market_data")
    val marketData: MarketData,
)

data class Image(
    val large: String
)

data class CurrentPrice(
    val usd: Double
)

data class AllTimeHigh(
    val usd: Double
)

data class AllTimeHighDate(
    val usd: String
)

data class AllTimeLow(
    val usd: Double
)

data class AllTimeLowDate(
    val usd: String
)

data class MarketCap(
    val usd: Long
)

data class DailyHigh(
    val usd: Double
)

data class DailyLow(
    val usd: Double
)

data class HistoricalPrices7d(
    @SerializedName("price")
    val usd: List<Double>
)

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: CurrentPrice,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,
    @SerializedName("sparkline_7d")
    val historicalPrices7d: HistoricalPrices7d,
    @SerializedName("high_24h")
    val dailyHigh: DailyHigh,
    @SerializedName("low_24h")
    val dailyLow: DailyLow,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("market_cap")
    val marketCap: MarketCap,
    @SerializedName("total_supply")
    val totalSupply: Double,
    @SerializedName("atl")
    val allTimeLow: AllTimeLow,
    @SerializedName("ath")
    val allTimeHigh: AllTimeHigh,
    @SerializedName("atl_date")
    val allTimeLowDate: AllTimeLowDate,
    @SerializedName("ath_date")
    val allTimeHighDate: AllTimeHighDate,
)

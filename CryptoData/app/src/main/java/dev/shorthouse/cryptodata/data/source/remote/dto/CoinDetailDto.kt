package dev.shorthouse.cryptodata.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class CoinDetailDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: Image,
    @SerializedName("genesis_date")
    val genesisDate: String,
    @SerializedName("market_data")
    val marketData: MarketData,
    val description: Description,
    val links: Links
)

data class Description(
    val en: String
)

data class Links(
    val homepage: List<String>,
    @SerializedName("subreddit_url")
    val subreddit: String?,
    @SerializedName("repos_url")
    val reposUrl: ReposUrl
)

data class ReposUrl(
    val github: List<String>
)

data class Image(
    val thumb: String,
    val small: String,
    val large: String
)

data class CurrentPrice(
    val gbp: Double
)

data class AllTimeHigh(
    val gbp: Double
)

data class AllTimeHighDate(
    val gbp: String
)

data class AllTimeLow(
    val gbp: Double
)

data class AllTimeLowDate(
    val gbp: String
)

data class MarketCap(
    val gbp: Long
)

data class DailyHigh(
    val gbp: Double
)

data class DailyLow(
    val gbp: Double
)

data class HistoricalPrices(
    @SerializedName("price")
    val usd: List<Double>
)

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: CurrentPrice,
    @SerializedName("price_change_percentage_24h")
    val priceChange: Double,
    @SerializedName("ath")
    val allTimeHigh: AllTimeHigh,
    @SerializedName("ath_date")
    val allTimeHighDate: AllTimeHighDate,
    @SerializedName("atl")
    val allTimeLow: AllTimeLow,
    @SerializedName("atl_date")
    val allTimeLowDate: AllTimeLowDate,
    @SerializedName("market_cap")
    val marketCap: MarketCap,
    @SerializedName("market_cap_change_percentage_24h")
    val marketCapChangePercentage: Double,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("high_24h")
    val dailyHigh: DailyHigh,
    @SerializedName("low_24h")
    val dailyLow: DailyLow,
    @SerializedName("sparkline_7d")
    val historicalPrices: HistoricalPrices,
)

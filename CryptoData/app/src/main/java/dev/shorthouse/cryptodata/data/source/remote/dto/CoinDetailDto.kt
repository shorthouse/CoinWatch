package dev.shorthouse.cryptodata.data.source.remote.dto

import com.google.gson.annotations.SerializedName
import dev.shorthouse.cryptodata.model.CoinDetail

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

fun CoinDetailDto.toCoinDetail(): CoinDetail {
    val currentPrice = marketData.currentPrice.gbp
    val dailyHigh = marketData.dailyHigh.gbp
    val dailyLow = marketData.dailyLow.gbp

    val dailyHighChangePercentage = 100 * (1 - (currentPrice / dailyHigh))
    val dailyLowChangePercentage = 100 * (currentPrice / dailyLow)

    return CoinDetail(
        id = id,
        symbol = symbol.uppercase(),
        name = name,
        image = image.large,
        currentPrice = currentPrice,
        priceChangePercentage = marketData.priceChange,
        genesisDate = genesisDate,
        //        allTimeHigh = marketData.allTimeHigh.gbp.toCurrencyString(),
        //        allTimeHighDate = marketData.allTimeHighDate.gbp,
        //        allTimeLow = marketData.allTimeLow.gbp.toCurrencyString(),
        //        allTimeLowDate = marketData.allTimeLowDate.gbp,
        marketCapRank = marketData.marketCapRank,
        marketCap = marketData.marketCap.gbp,
        marketCapChangePercentage = marketData.marketCapChangePercentage,
        dailyHigh = dailyHigh,
        dailyHighChangePercentage = dailyHighChangePercentage,
        dailyLow = dailyLow,
        dailyLowChangePercentage = dailyLowChangePercentage,

        description = description.en,
        homepageLink = links.homepage.firstOrNull(),
        githubLink = links.reposUrl.github.firstOrNull(),
        subredditLink = links.subreddit
    )
}

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
    val dailyLow: DailyLow
)

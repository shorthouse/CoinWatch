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
    val links: Links,
)

fun CoinDetailDto.toCoinDetail(): CoinDetail {
    return CoinDetail(
        id = id,
        symbol = symbol.uppercase(),
        name = name,
        image = image.large,
        genesisDate = genesisDate,
        currentPrice = marketData.currentPrice.gbp,
        priceChangePercentage = marketData.priceChange,
//        allTimeHigh = marketData.allTimeHigh.gbp.toCurrencyString(),
//        allTimeHighDate = marketData.allTimeHighDate.gbp,
//        allTimeLow = marketData.allTimeLow.gbp.toCurrencyString(),
//        allTimeLowDate = marketData.allTimeLowDate.gbp,
//        marketCap = marketData.marketCap.gbp,
//        marketCapChangePercentage = marketData.marketCapChangePercentage.toPercentageString(),
//        marketCapRank = marketData.marketCapRank,
        description = description.en,
        homepageLink = links.homepage.firstOrNull(),
        blockchainLink = links.blockchainSite.firstOrNull(),
        subredditLink = links.subreddit,
    )
}

data class Description(
    val en: String,
)

data class Links(
    val homepage: List<String?>,
    @SerializedName("blockchain_site")
    val blockchainSite: List<String?>,
    @SerializedName("subreddit_url")
    val subreddit: String?,
)

data class Image(
    val thumb: String,
    val small: String,
    val large: String,
)

data class CurrentPrice(
    val gbp: Double,
)

data class AllTimeHigh(
    val gbp: Double,
)

data class AllTimeHighDate(
    val gbp: String,
)

data class AllTimeLow(
    val gbp: Double,
)

data class AllTimeLowDate(
    val gbp: String,
)

data class MarketCap(
    val gbp: Long,
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
)

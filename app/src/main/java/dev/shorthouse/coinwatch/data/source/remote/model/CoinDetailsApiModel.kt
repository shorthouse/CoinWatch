package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class CoinDetailsApiModel(
    @SerializedName("data")
    val coinDetailsDataHolder: CoinDetailsDataHolder?
)

data class CoinDetailsDataHolder(
    @SerializedName("coin")
    val coinDetailsData: CoinDetailsData?
)

data class CoinDetailsData(
    @SerializedName("uuid")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("websiteUrl")
    val websiteUrl: String?,
    @SerializedName("iconUrl")
    val imageUrl: String?,
    @SerializedName("price")
    val currentPrice: String?,
    @SerializedName("marketCap")
    val marketCap: String?,
    @SerializedName("fullyDilutedMarketCap")
    val fullyDilutedMarketCap: String?,
    @SerializedName("rank")
    val marketCapRank: String?,
    @SerializedName("24hVolume")
    val volume24h: String?,
    @SerializedName("numberOfMarkets")
    val numberOfMarkets: Int?,
    @SerializedName("numberOfExchanges")
    val numberOfExchanges: Int?,
    @SerializedName("supply")
    val supply: Supply?,
    @SerializedName("allTimeHigh")
    val allTimeHigh: AllTimeHigh?,
    @SerializedName("tags")
    val tags: List<String>?,
    @SerializedName("links")
    val links: List<CoinDetailsLink>?,
    @SerializedName("coinrankingUrl")
    val coinrankingUrl: String?,
    @SerializedName("listedAt")
    val listedAt: Long?
)

data class CoinDetailsLink(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("type")
    val type: String?
)

data class Supply(
    @SerializedName("circulating")
    val circulatingSupply: String?,
    @SerializedName("total")
    val totalSupply: String?,
    @SerializedName("max")
    val maxSupply: String?
)

data class AllTimeHigh(
    @SerializedName("price")
    val price: String?,
    @SerializedName("timestamp")
    val timestamp: Long?
)

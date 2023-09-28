package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class CoinSearchResultsApiModel(
    @SerializedName("data")
    val coinsSearchResultsData: CoinSearchResultsData
)

data class CoinSearchResultsData(
    @SerializedName("coins")
    val coinSearchResults: List<CoinSearch?>?
)

data class CoinSearch(
    @SerializedName("uuid")
    val id: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("iconUrl")
    val iconUrl: String?,
    @SerializedName("price")
    val currentPrice: String?
)

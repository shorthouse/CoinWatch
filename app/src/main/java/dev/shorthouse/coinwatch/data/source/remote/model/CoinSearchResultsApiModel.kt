package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class CoinSearchResultsApiModel(
    @SerializedName("data")
    val coinsSearchResultsData: CoinSearchResultsData?
)

data class CoinSearchResultsData(
    @SerializedName("coins")
    val coinSearchResults: List<CoinSearchResult?>?
)

data class CoinSearchResult(
    @SerializedName("uuid")
    val id: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("iconUrl")
    val imageUrl: String?
)

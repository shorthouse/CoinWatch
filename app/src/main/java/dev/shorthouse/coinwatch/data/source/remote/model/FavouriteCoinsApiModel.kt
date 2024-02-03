package dev.shorthouse.coinwatch.data.source.remote.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class FavouriteCoinsApiModel(
    @SerializedName("data")
    val favouriteCoinsData: FavouriteCoinsData?
)

data class FavouriteCoinsData(
    @SerializedName("coins")
    val favouriteCoins: List<FavouriteCoinApiModel?>?
)

data class FavouriteCoinApiModel(
    @SerializedName("uuid")
    val id: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("iconUrl")
    val imageUrl: String?,
    @SerializedName("price")
    val currentPrice: String?,
    @SerializedName("change")
    val priceChangePercentage24h: String?,
    @SerializedName("sparkline")
    val prices24h: List<BigDecimal?>?
)

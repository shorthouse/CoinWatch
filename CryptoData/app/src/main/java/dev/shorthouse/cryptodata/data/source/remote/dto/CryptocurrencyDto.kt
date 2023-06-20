package dev.shorthouse.cryptodata.data.source.remote.dto

import com.google.gson.annotations.SerializedName
import dev.shorthouse.cryptodata.model.Coin

data class CryptocurrencyDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage: Double,
)

fun CryptocurrencyDto.toCryptocurrency(): Coin {
    return Coin(
        id = id,
        symbol = symbol.uppercase(),
        name = name,
        image = image,
        currentPrice = currentPrice,
        priceChangePercentage = priceChangePercentage,
    )
}

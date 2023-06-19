package dev.shorthouse.cryptodata.data.source.remote.dto

import com.google.gson.annotations.SerializedName
import dev.shorthouse.cryptodata.model.Cryptocurrency
import java.math.RoundingMode

data class CryptocurrencyDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("price_change_percentage_24h")
    val priceChange: Double,
)

fun CryptocurrencyDto.toCryptocurrency(): Cryptocurrency {
    val priceChangeSign = if (currentPrice >= 0) '+' else '-'
    val priceChangeRounded = priceChange.toBigDecimal()
        .setScale(2, RoundingMode.HALF_EVEN)
        .toString()

    return Cryptocurrency(
        id = id,
        symbol = symbol.uppercase(),
        name = name,
        image = image,
        currentPrice = "£" + currentPrice,
        priceChange = priceChangeSign + priceChangeRounded + '%',
    )
}

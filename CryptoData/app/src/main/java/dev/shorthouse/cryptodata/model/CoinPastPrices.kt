package dev.shorthouse.cryptodata.model

import dev.shorthouse.cryptodata.data.source.remote.model.CoinPastPricesApiModel
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

data class CoinPastPrices(
    val prices: List<Double>,
    val minPrice: String,
    val minPriceChangePercentage: Double,
    val maxPrice: String,
    val maxPriceChangePercentage: Double
)

fun CoinPastPricesApiModel.toCoinPastPrices(): CoinPastPrices {
    val prices = this.prices.map { it.last() }

    val minPrice = prices.min()
    val maxPrice = prices.max()
    val currentPrice = this.prices.last().last()

    val minPriceChangePercentage = ((currentPrice - minPrice) / minPrice) * 100
    val maxPriceChangePercentage = ((currentPrice - maxPrice) / maxPrice) * 100

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US).apply {
        currency = Currency.getInstance("USD")
    }

    return CoinPastPrices(
        prices = prices,
        minPrice = currencyFormatter.format(minPrice),
        minPriceChangePercentage = minPriceChangePercentage,
        maxPrice = currencyFormatter.format(maxPrice),
        maxPriceChangePercentage = maxPriceChangePercentage
    )
}

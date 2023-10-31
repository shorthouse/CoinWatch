package dev.shorthouse.coinwatch.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val currency: Currency = Currency.USD,
    val coinSort: CoinSort = CoinSort.MarketCap
)

enum class Currency(val symbol: String) {
    USD("$"),
    GBP("£"),
    EUR("€")
}

enum class CoinSort {
    MarketCap,
    Price,
    PriceChange24h,
    Volume24h
}

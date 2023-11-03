package dev.shorthouse.coinwatch.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val coinSort: CoinSort = CoinSort.MarketCap,
    val currency: Currency = Currency.USD
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

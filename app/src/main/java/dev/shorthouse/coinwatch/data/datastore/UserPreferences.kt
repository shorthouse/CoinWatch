package dev.shorthouse.coinwatch.data.datastore

import androidx.annotation.StringRes
import dev.shorthouse.coinwatch.R
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val currency: Currency = Currency.USD,
    val coinSort: CoinSort = CoinSort.MarketCap
)

enum class Currency(@StringRes val nameStringId: Int) {
    USD(R.string.currency_usd),
    GBP(R.string.currency_gbp),
    EUR(R.string.currency_eur)
}

enum class CoinSort {
    MarketCap,
    Price,
    PriceChange24h,
    Volume24h
}

package dev.shorthouse.coinwatch.data.datastore

import androidx.annotation.StringRes
import dev.shorthouse.coinwatch.R
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val currency: Currency = Currency.USD
)

enum class Currency(@StringRes val nameStringId: Int) {
    USD(R.string.currency_usd),
    GBP(R.string.currency_gbp),
    EUR(R.string.currency_eur)
}

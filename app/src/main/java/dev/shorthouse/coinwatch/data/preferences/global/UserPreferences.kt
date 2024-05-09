package dev.shorthouse.coinwatch.data.preferences.global

import androidx.annotation.StringRes
import dev.shorthouse.coinwatch.R
import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val currency: Currency = Currency.USD,
    val startScreen: StartScreen = StartScreen.Market,
)

enum class Currency(val symbol: String, @StringRes val nameId: Int) {
    USD("$", R.string.currency_usd),
    GBP("£", R.string.currency_gbp),
    EUR("€", R.string.currency_eur)
}

enum class StartScreen(@StringRes val nameId: Int) {
    Market(R.string.market_screen),
    Favourites(R.string.favourites_screen),
    Search(R.string.search_screen)
}

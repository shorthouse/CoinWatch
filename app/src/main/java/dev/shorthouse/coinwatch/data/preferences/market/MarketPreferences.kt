package dev.shorthouse.coinwatch.data.preferences.market

import androidx.annotation.StringRes
import dev.shorthouse.coinwatch.R
import kotlinx.serialization.Serializable

@Serializable
data class MarketPreferences(
    val marketCoinSort: MarketCoinSort = MarketCoinSort.MarketCap
)

enum class MarketCoinSort(@StringRes val nameId: Int) {
    MarketCap(R.string.market_coin_sort_market_cap),
    Popular(R.string.market_coin_sort_popular),
    Gainers(R.string.market_coin_sort_gainers),
    Losers(R.string.market_coin_sort_losers),
    Newest(R.string.market_coin_sort_newest),
}

package dev.shorthouse.coinwatch.data.preferences.common

import androidx.annotation.StringRes
import dev.shorthouse.coinwatch.R

enum class CoinSort(@StringRes val nameId: Int) {
    MarketCap(R.string.market_coin_sort_market_cap),
    Popular(R.string.market_coin_sort_popular),
    Gainers(R.string.market_coin_sort_gainers),
    Losers(R.string.market_coin_sort_losers),
    Newest(R.string.market_coin_sort_newest),
}

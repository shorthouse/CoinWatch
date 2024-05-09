package dev.shorthouse.coinwatch.data.preferences.market

import dev.shorthouse.coinwatch.data.preferences.common.CoinSort
import kotlinx.serialization.Serializable

@Serializable
data class MarketPreferences(
    val coinSort: CoinSort = CoinSort.MarketCap
)

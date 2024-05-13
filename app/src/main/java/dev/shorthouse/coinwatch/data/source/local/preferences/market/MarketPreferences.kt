package dev.shorthouse.coinwatch.data.source.local.preferences.market

import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import kotlinx.serialization.Serializable

@Serializable
data class MarketPreferences(
    val coinSort: CoinSort = CoinSort.MarketCap
)

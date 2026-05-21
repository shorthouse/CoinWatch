package dev.shorthouse.coinwatch.data.source.local.datastore.market

import dev.shorthouse.coinwatch.data.source.local.datastore.common.CoinSort
import kotlinx.serialization.Serializable

@Serializable
data class MarketPreferences(
    val coinSort: CoinSort = CoinSort.MarketCap,
)

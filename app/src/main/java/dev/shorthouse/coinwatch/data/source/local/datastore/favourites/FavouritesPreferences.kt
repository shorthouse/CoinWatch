package dev.shorthouse.coinwatch.data.source.local.datastore.favourites

import dev.shorthouse.coinwatch.data.source.local.datastore.common.CoinSort
import kotlinx.serialization.Serializable

@Serializable
data class FavouritesPreferences(
    val isFavouritesCondensed: Boolean = false,
    val coinSort: CoinSort = CoinSort.MarketCap,
)

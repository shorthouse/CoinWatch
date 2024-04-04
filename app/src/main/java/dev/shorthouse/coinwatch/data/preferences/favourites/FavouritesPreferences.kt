package dev.shorthouse.coinwatch.data.preferences.favourites

import dev.shorthouse.coinwatch.data.preferences.common.CoinSort
import kotlinx.serialization.Serializable

@Serializable
data class FavouritesPreferences(
    val isFavouritesCondensed: Boolean = false,
    val coinSort: CoinSort = CoinSort.MarketCap
)

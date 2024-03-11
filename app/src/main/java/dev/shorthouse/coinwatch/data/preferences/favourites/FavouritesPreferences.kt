package dev.shorthouse.coinwatch.data.preferences.favourites

import kotlinx.serialization.Serializable

@Serializable
data class FavouritesPreferences(
    val isFavouritesCondensed: Boolean = false
)

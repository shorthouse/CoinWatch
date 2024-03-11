package dev.shorthouse.coinwatch.data.preferences.favourites

import dev.shorthouse.coinwatch.data.preferences.BasePreferencesSerializer

object FavouritesPreferencesSerializer : BasePreferencesSerializer<FavouritesPreferences>(
    defaultInstance = { FavouritesPreferences() },
    serializer = FavouritesPreferences.serializer(),
)

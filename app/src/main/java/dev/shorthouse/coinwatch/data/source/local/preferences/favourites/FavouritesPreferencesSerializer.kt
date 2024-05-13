package dev.shorthouse.coinwatch.data.source.local.preferences.favourites

import dev.shorthouse.coinwatch.data.source.local.preferences.BasePreferencesSerializer

object FavouritesPreferencesSerializer : BasePreferencesSerializer<FavouritesPreferences>(
    defaultInstance = { FavouritesPreferences() },
    serializer = FavouritesPreferences.serializer(),
)

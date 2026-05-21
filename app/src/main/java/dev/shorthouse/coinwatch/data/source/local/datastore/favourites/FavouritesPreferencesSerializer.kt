package dev.shorthouse.coinwatch.data.source.local.datastore.favourites

import dev.shorthouse.coinwatch.data.source.local.datastore.BasePreferencesSerializer

object FavouritesPreferencesSerializer : BasePreferencesSerializer<FavouritesPreferences>(
    defaultInstance = { FavouritesPreferences() },
    serializer = FavouritesPreferences.serializer(),
)

package dev.shorthouse.coinwatch.data.source.local.datastore.global

import dev.shorthouse.coinwatch.data.source.local.datastore.BasePreferencesSerializer

object UserPreferencesSerializer : BasePreferencesSerializer<UserPreferences>(
    defaultInstance = { UserPreferences() },
    serializer = UserPreferences.serializer(),
)

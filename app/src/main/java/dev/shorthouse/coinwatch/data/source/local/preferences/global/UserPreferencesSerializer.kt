package dev.shorthouse.coinwatch.data.source.local.preferences.global

import dev.shorthouse.coinwatch.data.source.local.preferences.BasePreferencesSerializer

object UserPreferencesSerializer : BasePreferencesSerializer<UserPreferences>(
    defaultInstance = { UserPreferences() },
    serializer = UserPreferences.serializer(),
)

package dev.shorthouse.coinwatch.data.preferences.global

import dev.shorthouse.coinwatch.data.preferences.BasePreferencesSerializer

object UserPreferencesSerializer : BasePreferencesSerializer<UserPreferences>(
    defaultInstance = { UserPreferences() },
    serializer = UserPreferences.serializer(),
)

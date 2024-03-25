package dev.shorthouse.coinwatch.data.preferences.market

import dev.shorthouse.coinwatch.data.preferences.BasePreferencesSerializer

object MarketPreferencesSerializer : BasePreferencesSerializer<MarketPreferences>(
    defaultInstance = { MarketPreferences() },
    serializer = MarketPreferences.serializer()
)

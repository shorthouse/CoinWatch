package dev.shorthouse.coinwatch.data.source.local.preferences.market

import dev.shorthouse.coinwatch.data.source.local.preferences.BasePreferencesSerializer

object MarketPreferencesSerializer : BasePreferencesSerializer<MarketPreferences>(
    defaultInstance = { MarketPreferences() },
    serializer = MarketPreferences.serializer()
)

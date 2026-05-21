package dev.shorthouse.coinwatch.data.source.local.datastore.market

import dev.shorthouse.coinwatch.data.source.local.datastore.BasePreferencesSerializer

object MarketPreferencesSerializer : BasePreferencesSerializer<MarketPreferences>(
    defaultInstance = { MarketPreferences() },
    serializer = MarketPreferences.serializer()
)

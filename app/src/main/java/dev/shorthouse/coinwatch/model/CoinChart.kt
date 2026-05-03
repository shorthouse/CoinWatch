package dev.shorthouse.coinwatch.model

import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import kotlinx.collections.immutable.ImmutableList

data class CoinChart(
    val currency: Currency,
    val priceHistory: ImmutableList<PriceEntry>,
    val periodPriceChangePercentage: Percentage
)

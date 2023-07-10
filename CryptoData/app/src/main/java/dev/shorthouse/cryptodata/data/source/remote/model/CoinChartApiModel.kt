package dev.shorthouse.cryptodata.data.source.remote.model

import java.math.BigDecimal

data class CoinChartApiModel(
    val prices: List<List<BigDecimal>>
)

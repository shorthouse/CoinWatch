package dev.shorthouse.coinwatch.model

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

data class GlobalMarket(
    val totalMarketCap: Price,
    val volume24h: Price,
    val btcDominancePercentage: BigDecimal,
    val coinsUp24h: Int,
    val coinsDown24h: Int,
) {
    val btcDominanceFraction: Float
        get() = btcDominancePercentage
            .coerceIn(BigDecimal.ZERO, BigDecimal("100"))
            .divide(BigDecimal("100"))
            .toFloat()

    val formattedBtcDominance: String
        get() = PercentageFormatter.formatUnsigned(
            amount = btcDominancePercentage,
            formatLocale = formatLocale
        )

    val formattedCoinsUp24h: String
        get() = NumberFormat.getIntegerInstance(formatLocale).format(coinsUp24h)

    val formattedCoinsDown24h: String
        get() = NumberFormat.getIntegerInstance(formatLocale).format(coinsDown24h)

    val coinsUpWeight: Float
        get() = if (coinsUp24h + coinsDown24h > 0) coinsUp24h.toFloat() else 1f

    val coinsDownWeight: Float
        get() = if (coinsUp24h + coinsDown24h > 0) coinsDown24h.toFloat() else 1f

    private val formatLocale: Locale
        get() = Locale.getDefault(Locale.Category.FORMAT)
}

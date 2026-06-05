package dev.shorthouse.coinwatch.model

import kotlinx.collections.immutable.ImmutableList
import java.math.BigDecimal

data class FearGreed(
    val value: Int,
    val moodBand: FearGreedMoodBand,
    val history: ImmutableList<BigDecimal>,
)

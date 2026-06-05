package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.data.source.remote.model.FearGreedApiModel
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import kotlinx.collections.immutable.toPersistentList
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.math.roundToInt

class FearGreedMapper @Inject constructor() {
    fun mapApiModelToModel(apiModel: FearGreedApiModel): FearGreed? {
        val values = apiModel.data
            .orEmpty()
            .filterNotNull()
            .mapNotNull { dataPoint ->
                val timestamp = dataPoint.timestamp ?: return@mapNotNull null
                val value = dataPoint.value ?: return@mapNotNull null

                timestamp to value.roundToInt().coerceIn(0, 100)
            }
            .sortedBy { it.first }
            .map { it.second }

        return values
            .takeIf { it.isNotEmpty() }
            ?.let {
                val latestValue = it.last()

                FearGreed(
                    value = latestValue,
                    moodBand = when (latestValue) {
                        in 0..25 -> FearGreedMoodBand.ExtremeFear
                        in 26..46 -> FearGreedMoodBand.Fear
                        in 47..54 -> FearGreedMoodBand.Neutral
                        in 55..75 -> FearGreedMoodBand.Greed
                        else -> FearGreedMoodBand.ExtremeGreed
                    },
                    history = it
                        .map { value -> BigDecimal.valueOf(value.toLong()) }
                        .toPersistentList()
                )
            }
    }
}

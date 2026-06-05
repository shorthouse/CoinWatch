package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.remote.model.FearGreedApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FearGreedDataPointApiModel
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class FearGreedMapperTest {

    private val fearGreedMapper = FearGreedMapper()

    @Test
    fun `When fear greed data is valid should map sorted history and latest value`() {
        val apiModel = FearGreedApiModel(
            data = listOf(
                FearGreedDataPointApiModel(timestamp = 300L, value = 39.3),
                FearGreedDataPointApiModel(timestamp = 100L, value = 20.1),
                FearGreedDataPointApiModel(timestamp = 200L, value = 30.6)
            )
        )

        val result = fearGreedMapper.mapApiModelToModel(apiModel)

        assertThat(result).isEqualTo(
            FearGreed(
                value = 39,
                moodBand = FearGreedMoodBand.Fear,
                history = persistentListOf(
                    BigDecimal("20"),
                    BigDecimal("31"),
                    BigDecimal("39")
                )
            )
        )
    }

    @Test
    fun `When fear greed values exceed bounds should round and clamp values`() {
        val apiModel = FearGreedApiModel(
            data = listOf(
                FearGreedDataPointApiModel(timestamp = 100L, value = -5.4),
                FearGreedDataPointApiModel(timestamp = 200L, value = 50.5),
                FearGreedDataPointApiModel(timestamp = 300L, value = 120.9)
            )
        )

        val result = fearGreedMapper.mapApiModelToModel(apiModel)

        assertThat(result).isEqualTo(
            FearGreed(
                value = 100,
                moodBand = FearGreedMoodBand.ExtremeGreed,
                history = persistentListOf(
                    BigDecimal("0"),
                    BigDecimal("51"),
                    BigDecimal("100")
                )
            )
        )
    }

    @Test
    fun `When fear greed data has null values should filter these values`() {
        val apiModel = FearGreedApiModel(
            data = listOf(
                FearGreedDataPointApiModel(timestamp = 100L, value = 21.0),
                FearGreedDataPointApiModel(timestamp = null, value = 30.0),
                FearGreedDataPointApiModel(timestamp = 200L, value = null),
                null,
                FearGreedDataPointApiModel(timestamp = 300L, value = 44.0)
            )
        )

        val result = fearGreedMapper.mapApiModelToModel(apiModel)

        assertThat(result).isEqualTo(
            FearGreed(
                value = 44,
                moodBand = FearGreedMoodBand.Fear,
                history = persistentListOf(
                    BigDecimal("21"),
                    BigDecimal("44")
                )
            )
        )
    }

    @Test
    fun `When fear greed data contains boundary values should preserve integer decimal history`() {
        val apiModel = FearGreedApiModel(
            data = listOf(
                FearGreedDataPointApiModel(timestamp = 100L, value = 25.0),
                FearGreedDataPointApiModel(timestamp = 200L, value = 26.0),
                FearGreedDataPointApiModel(timestamp = 300L, value = 46.0),
                FearGreedDataPointApiModel(timestamp = 400L, value = 47.0),
                FearGreedDataPointApiModel(timestamp = 500L, value = 54.0),
                FearGreedDataPointApiModel(timestamp = 600L, value = 55.0),
                FearGreedDataPointApiModel(timestamp = 700L, value = 75.0),
                FearGreedDataPointApiModel(timestamp = 800L, value = 76.0)
            )
        )

        val result = fearGreedMapper.mapApiModelToModel(apiModel)

        assertThat(result).isEqualTo(
            FearGreed(
                value = 76,
                moodBand = FearGreedMoodBand.ExtremeGreed,
                history = persistentListOf(
                    BigDecimal("25"),
                    BigDecimal("26"),
                    BigDecimal("46"),
                    BigDecimal("47"),
                    BigDecimal("54"),
                    BigDecimal("55"),
                    BigDecimal("75"),
                    BigDecimal("76")
                )
            )
        )
    }

    @Test
    fun `When mapping latest value should classify into expected mood band`() {
        mapOf(
            0 to FearGreedMoodBand.ExtremeFear,
            25 to FearGreedMoodBand.ExtremeFear,
            26 to FearGreedMoodBand.Fear,
            46 to FearGreedMoodBand.Fear,
            47 to FearGreedMoodBand.Neutral,
            54 to FearGreedMoodBand.Neutral,
            55 to FearGreedMoodBand.Greed,
            75 to FearGreedMoodBand.Greed,
            76 to FearGreedMoodBand.ExtremeGreed,
            100 to FearGreedMoodBand.ExtremeGreed
        ).forEach { (value, expectedMoodBand) ->
            val apiModel = FearGreedApiModel(
                data = listOf(
                    FearGreedDataPointApiModel(timestamp = 100L, value = value.toDouble())
                )
            )

            val result = fearGreedMapper.mapApiModelToModel(apiModel)

            assertThat(result?.moodBand).isEqualTo(expectedMoodBand)
        }
    }

    @Test
    fun `When fear greed data has no valid values should return null`() {
        val apiModel = FearGreedApiModel(
            data = listOf(
                FearGreedDataPointApiModel(timestamp = null, value = 30.0),
                FearGreedDataPointApiModel(timestamp = 200L, value = null),
                null
            )
        )

        val result = fearGreedMapper.mapApiModelToModel(apiModel)

        assertThat(result).isNull()
    }

    @Test
    fun `When fear greed data is null should return null`() {
        val apiModel = FearGreedApiModel(
            data = null
        )

        val result = fearGreedMapper.mapApiModelToModel(apiModel)

        assertThat(result).isNull()
    }
}

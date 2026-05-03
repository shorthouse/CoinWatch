package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrNull
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.PriceEntry
import kotlinx.collections.immutable.toPersistentList
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CoinChartMapper @Inject constructor() {

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val dateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")

    fun mapApiModelToModel(
        apiModel: CoinChartApiModel,
        zone: ZoneId = ZoneId.systemDefault(),
        today: LocalDate = LocalDate.now(zone)
    ): CoinChart {
        val validEntries = apiModel.coinChartData?.pastPrices
            .orEmpty()
            .mapNotNull { pastPrice ->
                val amount = pastPrice?.amount.toSanitisedBigDecimalOrNull() ?: return@mapNotNull null
                val timestamp = pastPrice?.timestamp ?: return@mapNotNull null
                if (amount < BigDecimal.ZERO) return@mapNotNull null
                PriceEntry(
                    price = amount,
                    timestamp = timestamp,
                    formattedDate = formatTimestamp(timestamp, zone, today)
                )
            }
            .reversed()

        return CoinChart(
            priceHistory = validEntries.toPersistentList(),
            periodPriceChangePercentage = Percentage(apiModel.coinChartData?.priceChangePercentage)
        )
    }

    private fun formatTimestamp(epochSecond: Long, zone: ZoneId, today: LocalDate): String {
        val instant = Instant.ofEpochSecond(epochSecond)
        val scrubDate = instant.atZone(zone).toLocalDate()

        return when (scrubDate) {
            today -> timeFormatter.withZone(zone).format(instant)
            today.minusDays(1) -> "Yesterday ${timeFormatter.withZone(zone).format(instant)}"
            else -> dateFormatter.withZone(zone).format(instant)
        }
    }
}

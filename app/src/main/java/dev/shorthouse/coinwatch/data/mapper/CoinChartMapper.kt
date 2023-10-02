package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.common.Mapper
import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrNull
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList

class CoinChartMapper @Inject constructor() : Mapper<CoinChartApiModel, CoinChart> {
    override fun mapApiModelToModel(from: CoinChartApiModel): CoinChart {
        val validPrices = from.coinChartData?.pastPrices
            .orEmpty()
            .mapNotNull { pastPrice ->
                pastPrice?.amount.toSanitisedBigDecimalOrNull()
            }
            .reversed()

        val minPrice = when {
            validPrices.isNotEmpty() -> validPrices.minOrNull().toString()
            else -> null
        }

        val maxPrice = when {
            validPrices.isNotEmpty() -> validPrices.maxOrNull().toString()
            else -> null
        }

        return CoinChart(
            prices = validPrices.toPersistentList(),
            minPrice = Price(minPrice),
            maxPrice = Price(maxPrice),
            periodPriceChangePercentage = Percentage(from.coinChartData?.pricePercentageChange)
        )
    }
}

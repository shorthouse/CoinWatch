package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrNull
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList

class CoinChartMapper @Inject constructor() {
    fun mapApiModelToModel(apiModel: CoinChartApiModel, currency: Currency): CoinChart {
        val validPrices = apiModel.coinChartData?.pastPrices
            .orEmpty()
            .mapNotNull { pastPrice ->
                pastPrice?.amount.toSanitisedBigDecimalOrNull()
            }
            .filter { price -> price.compareTo(BigDecimal.ZERO) >= 0 }
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
            minPrice = Price(minPrice, currency = currency),
            maxPrice = Price(maxPrice, currency = currency),
            periodPriceChangePercentage = Percentage(apiModel.coinChartData?.priceChangePercentage)
        )
    }
}

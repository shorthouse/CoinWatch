package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrNull
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.TrendingCoinsApiModel
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.TrendingCoin
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

class TrendingCoinMapper @Inject constructor() {
    fun mapApiModelToModel(apiModel: TrendingCoinsApiModel, currency: Currency): List<TrendingCoin> {
        val validCoins = apiModel.trendingCoinsData?.coins
            .orEmpty()
            .filterNotNull()
            .filter { it.id != null }
            .sortedBy { it.rank ?: Int.MAX_VALUE }
            .take(TRENDING_COINS_LIMIT)

        return validCoins.map { trendingCoinApiModel ->
            TrendingCoin(
                id = trendingCoinApiModel.id!!,
                name = trendingCoinApiModel.name.orEmpty(),
                symbol = trendingCoinApiModel.symbol.orEmpty(),
                imageUrl = trendingCoinApiModel.imageUrl.orEmpty(),
                currentPrice = Price(trendingCoinApiModel.currentPrice, currency = currency),
                priceChangePercentage24h = Percentage(trendingCoinApiModel.priceChangePercentage24h),
                sparkline = trendingCoinApiModel.sparkline
                    .orEmpty()
                    .mapNotNull { it.toSanitisedBigDecimalOrNull() }
                    .toPersistentList(),
            )
        }
    }

    private companion object {
        const val TRENDING_COINS_LIMIT = 10
    }
}

package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrNull
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.MoverCoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MoversApiModel
import dev.shorthouse.coinwatch.model.MoverCoin
import dev.shorthouse.coinwatch.model.Movers
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

class MoversMapper @Inject constructor() {
    fun mapApiModelToModel(
        gainersApiModel: MoversApiModel,
        losersApiModel: MoversApiModel,
        currency: Currency,
    ): Movers? {
        val gainers = gainersApiModel.validCoins().map { it.toMoverCoin(currency) }
        val losers = losersApiModel.validCoins().map { it.toMoverCoin(currency) }

        val topGainer = gainers.firstOrNull() ?: return null
        val topLoser = losers.firstOrNull() ?: return null

        val featuredIds = setOf(topGainer.id, topLoser.id)
        val mostMovement = (gainers.drop(1) + losers.drop(1))
            .distinctBy { it.id }
            .filterNot { it.id in featuredIds }
            .sortedByDescending { it.priceChangePercentage24h.amount.abs() }
            .take(MOST_MOVEMENT_LIMIT)
            .toPersistentList()

        return Movers(
            topGainer = topGainer,
            topLoser = topLoser,
            mostMovement = mostMovement
        )
    }

    private fun MoversApiModel.validCoins(): List<MoverCoinApiModel> {
        return moversData?.coins
            .orEmpty()
            .filterNotNull()
            .filter { !it.id.isNullOrBlank() }
    }

    private fun MoverCoinApiModel.toMoverCoin(currency: Currency): MoverCoin {
        return MoverCoin(
            id = id!!,
            name = name.orEmpty(),
            symbol = symbol.orEmpty(),
            imageUrl = imageUrl.orEmpty(),
            currentPrice = Price(currentPrice, currency = currency),
            priceChangePercentage24h = Percentage(priceChangePercentage24h),
            sparkline = sparkline
                .orEmpty()
                .mapNotNull { it.toSanitisedBigDecimalOrNull() }
                .toPersistentList(),
        )
    }

    private companion object {
        const val MOST_MOVEMENT_LIMIT = 5
    }
}

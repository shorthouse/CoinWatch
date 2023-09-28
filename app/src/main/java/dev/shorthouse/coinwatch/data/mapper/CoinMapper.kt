package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.common.Mapper
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList

class CoinMapper @Inject constructor() : Mapper<CoinsApiModel, List<Coin>> {
    override fun mapApiModelToModel(from: CoinsApiModel): List<Coin> {
        val sanitisedCoins = from.coinsData?.coins
            .orEmpty()
            .filterNotNull()
            .filter { it.id != null }

        return sanitisedCoins.map { coinApiModel ->
            Coin(
                id = coinApiModel.id!!,
                name = coinApiModel.name.orEmpty(),
                symbol = coinApiModel.symbol.orEmpty(),
                imageUrl = "",
                currentPrice = Price(coinApiModel.currentPrice),
                priceChangePercentage24h = Percentage(coinApiModel.priceChangePercentage24h),
                prices24h = coinApiModel.sparkline24h?.filterNotNull()
                    .orEmpty()
                    .toPersistentList()
            )
        }
    }
}

package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.data.source.local.model.Coin
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import javax.inject.Inject

class CoinMapper @Inject constructor() {
    fun mapApiModelToModel(apiModel: CoinsApiModel, currency: Currency): List<Coin> {
        val validCoins = apiModel.coinsData?.coins
            .orEmpty()
            .filterNotNull()
            .filter { it.id != null }

        return validCoins.map { coinApiModel ->
            Coin(
                id = coinApiModel.id!!,
                name = coinApiModel.name.orEmpty(),
                symbol = coinApiModel.symbol.orEmpty(),
                imageUrl = coinApiModel.imageUrl.orEmpty(),
                currentPrice = Price(coinApiModel.currentPrice, currency = currency),
                priceChangePercentage24h = Percentage(coinApiModel.priceChangePercentage24h),
            )
        }
    }
}

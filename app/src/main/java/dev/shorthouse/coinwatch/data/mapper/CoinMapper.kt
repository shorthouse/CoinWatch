package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList

class CoinMapper @Inject constructor() {
    fun mapApiModelToCachedModel(apiModel: CoinsApiModel, currency: Currency): List<CachedCoin> {
        val validCoins = apiModel.coinsData?.coins
            .orEmpty()
            .filterNotNull()
            .filter { it.id != null }

        return validCoins.map { coinApiModel ->
            CachedCoin(
                id = coinApiModel.id!!,
                name = coinApiModel.name.orEmpty(),
                symbol = coinApiModel.symbol.orEmpty(),
                imageUrl = coinApiModel.imageUrl.orEmpty(),
                currentPrice = Price(coinApiModel.currentPrice, currency = currency),
                priceChangePercentage24h = Percentage(coinApiModel.priceChangePercentage24h),
                prices24h = coinApiModel.prices24h
                    .orEmpty()
                    .filterNotNull()
                    .filter { price -> price.compareTo(BigDecimal.ZERO) >= 0 }
                    .toPersistentList()
            )
        }
    }
}

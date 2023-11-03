package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.data.datastore.Currency
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList

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
                prices24h = coinApiModel.prices24h
                    .orEmpty()
                    .filterNotNull()
                    .filter { price -> price.compareTo(BigDecimal.ZERO) >= 0 }
                    .toPersistentList()
            )
        }
    }

    fun mapCachedCoinToModel(cachedCoin: CachedCoin): Coin {
        return Coin(
            id = cachedCoin.id,
            name = cachedCoin.name,
            symbol = cachedCoin.symbol,
            imageUrl = cachedCoin.imageUrl,
            currentPrice = cachedCoin.currentPrice,
            priceChangePercentage24h = cachedCoin.priceChangePercentage24h,
            prices24h = cachedCoin.prices24h
        )
    }
}

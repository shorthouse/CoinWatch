package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList

class FavouriteCoinMapper @Inject constructor() {
    fun mapApiModelToModel(
        apiModel: FavouriteCoinsApiModel,
        currency: Currency
    ): List<FavouriteCoin> {
        val validFavouriteCoins = apiModel.favouriteCoinsData?.favouriteCoins
            .orEmpty()
            .filterNotNull()
            .filter { it.id != null }

        return validFavouriteCoins.map { favouriteCoinsApiModel ->
            FavouriteCoin(
                id = favouriteCoinsApiModel.id!!,
                name = favouriteCoinsApiModel.name.orEmpty(),
                symbol = favouriteCoinsApiModel.symbol.orEmpty(),
                imageUrl = favouriteCoinsApiModel.imageUrl.orEmpty(),
                currentPrice = Price(favouriteCoinsApiModel.currentPrice, currency = currency),
                priceChangePercentage24h = Percentage(
                    favouriteCoinsApiModel.priceChangePercentage24h
                ),
                prices24h = favouriteCoinsApiModel.prices24h
                    .orEmpty()
                    .filterNotNull()
                    .filter { price -> price.compareTo(BigDecimal.ZERO) >= 0 }
                    .toPersistentList()
            )
        }
    }
}

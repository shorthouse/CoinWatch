package dev.shorthouse.coinwatch.domain.favourites

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoinId
import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import javax.inject.Inject

class UpdateCachedFavouriteCoinsUseCase @Inject constructor(
    private val favouriteCoinRepository: FavouriteCoinRepository
) {
    suspend operator fun invoke(
        coinIds: List<FavouriteCoinId>,
        coinSort: CoinSort,
        currency: Currency,
    ): Result<List<FavouriteCoin>> {
        return updateCachedFavouriteCoins(
            coinIds = coinIds,
            coinSort = coinSort,
            currency = currency,
        )
    }

    private suspend fun updateCachedFavouriteCoins(
        coinIds: List<FavouriteCoinId>,
        currency: Currency,
        coinSort: CoinSort
    ): Result<List<FavouriteCoin>> {
        val remoteFavouriteCoinsResult = favouriteCoinRepository.getRemoteFavouriteCoins(
            coinIds = coinIds.map { it.id },
            coinSort = coinSort,
            currency = currency,
        )

        if (remoteFavouriteCoinsResult is Result.Success) {
            val favouriteCoins = remoteFavouriteCoinsResult.data
            favouriteCoinRepository.updateCachedFavouriteCoins(favouriteCoins = favouriteCoins)
        }

        return remoteFavouriteCoinsResult
    }
}

package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import dev.shorthouse.coinwatch.data.userPreferences.UserPreferences
import javax.inject.Inject

class UpdateCachedFavouriteCoinsUseCase @Inject constructor(
    private val favouriteCoinRepository: FavouriteCoinRepository
) {
    suspend operator fun invoke(
        coinIds: List<FavouriteCoinId>,
        userPreferences: UserPreferences
    ): Result<List<FavouriteCoin>> {
        return updateCachedFavouriteCoins(coinIds = coinIds, userPreferences = userPreferences)
    }

    private suspend fun updateCachedFavouriteCoins(
        coinIds: List<FavouriteCoinId>,
        userPreferences: UserPreferences
    ): Result<List<FavouriteCoin>> {
        val remoteFavouriteCoinsResult = favouriteCoinRepository.getRemoteFavouriteCoins(
            coinIds = coinIds.map { it.id },
            coinSort = userPreferences.coinSort,
            currency = userPreferences.currency
        )

        if (remoteFavouriteCoinsResult is Result.Success) {
            val favouriteCoins = remoteFavouriteCoinsResult.data
            favouriteCoinRepository.updateCachedFavouriteCoins(favouriteCoins = favouriteCoins)
        }

        return remoteFavouriteCoinsResult
    }
}

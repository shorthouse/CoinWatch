package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import javax.inject.Inject

class DeleteFavouriteCoinUseCase @Inject constructor(
    private val favouriteCoinRepository: FavouriteCoinRepository
) {
    suspend operator fun invoke(favouriteCoin: FavouriteCoin) {
        return deleteFavouriteCoin(favouriteCoin = favouriteCoin)
    }

    private suspend fun deleteFavouriteCoin(favouriteCoin: FavouriteCoin) {
        return favouriteCoinRepository.deleteFavouriteCoin(favouriteCoin = favouriteCoin)
    }
}

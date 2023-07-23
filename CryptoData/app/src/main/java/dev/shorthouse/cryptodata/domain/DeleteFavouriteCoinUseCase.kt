package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.cryptodata.data.source.local.model.FavouriteCoin
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

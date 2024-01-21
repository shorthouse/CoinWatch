package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import javax.inject.Inject

class ToggleIsCoinFavouriteUseCase @Inject constructor(
    private val favouriteCoinRepository: FavouriteCoinRepository
) {
    suspend operator fun invoke(favouriteCoin: FavouriteCoin) {
        return toggleIsCoinFavourite(favouriteCoin = favouriteCoin)
    }

    private suspend fun toggleIsCoinFavourite(favouriteCoin: FavouriteCoin) {
        return favouriteCoinRepository.toggleIsCoinFavourite(favouriteCoin = favouriteCoin)
    }
}

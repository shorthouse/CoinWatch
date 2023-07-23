package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.cryptodata.data.source.local.model.FavouriteCoin
import javax.inject.Inject

class InsertFavouriteCoinUseCase @Inject constructor(
    private val favouriteCoinRepository: FavouriteCoinRepository
) {
    suspend operator fun invoke(favouriteCoin: FavouriteCoin) {
        return insertFavouriteCoin(favouriteCoin = favouriteCoin)
    }

    private suspend fun insertFavouriteCoin(favouriteCoin: FavouriteCoin) {
        return favouriteCoinRepository.insertFavouriteCoin(favouriteCoin = favouriteCoin)
    }
}

package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.cryptodata.data.source.local.model.FavouriteCoin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouriteCoinsUseCase @Inject constructor(
    private val favouriteCoinRepository: FavouriteCoinRepository
) {
    operator fun invoke(): Flow<Result<List<FavouriteCoin>>> {
        return getFavouriteCoins()
    }

    private fun getFavouriteCoins(): Flow<Result<List<FavouriteCoin>>> {
        return favouriteCoinRepository.getFavouriteCoins()
    }
}

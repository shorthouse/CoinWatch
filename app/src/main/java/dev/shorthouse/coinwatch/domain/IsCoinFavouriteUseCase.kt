package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class IsCoinFavouriteUseCase @Inject constructor(
    private val favouriteCoinRepository: FavouriteCoinRepository
) {
    operator fun invoke(favouriteCoin: FavouriteCoin): Flow<Result<Boolean>> {
        return isCoinFavourite(favouriteCoin = favouriteCoin)
    }

    private fun isCoinFavourite(favouriteCoin: FavouriteCoin): Flow<Result<Boolean>> {
        return favouriteCoinRepository.isCoinFavourite(favouriteCoin = favouriteCoin)
    }
}

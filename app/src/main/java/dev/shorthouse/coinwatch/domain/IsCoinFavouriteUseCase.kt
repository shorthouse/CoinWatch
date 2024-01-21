package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.favouriteCoinId.FavouriteCoinIdRepository
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class IsCoinFavouriteUseCase @Inject constructor(
    private val favouriteCoinIdRepository: FavouriteCoinIdRepository
) {
    operator fun invoke(favouriteCoinId: FavouriteCoinId): Flow<Result<Boolean>> {
        return isCoinFavourite(favouriteCoinId = favouriteCoinId)
    }

    private fun isCoinFavourite(favouriteCoinId: FavouriteCoinId): Flow<Result<Boolean>> {
        return favouriteCoinIdRepository.isCoinFavourite(favouriteCoinId = favouriteCoinId)
    }
}

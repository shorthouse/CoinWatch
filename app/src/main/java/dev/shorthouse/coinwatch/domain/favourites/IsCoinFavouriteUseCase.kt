package dev.shorthouse.coinwatch.domain.favourites

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.favouriteCoinId.FavouriteCoinIdRepository
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoinId
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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

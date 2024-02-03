package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.favouriteCoinId.FavouriteCoinIdRepository
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetFavouriteCoinIdsUseCase @Inject constructor(
    private val favouriteCoinIdRepository: FavouriteCoinIdRepository
) {
    operator fun invoke(): Flow<Result<List<FavouriteCoinId>>> {
        return getFavouriteCoinIds()
    }

    private fun getFavouriteCoinIds(): Flow<Result<List<FavouriteCoinId>>> {
        return favouriteCoinIdRepository.getFavouriteCoinIds()
    }
}

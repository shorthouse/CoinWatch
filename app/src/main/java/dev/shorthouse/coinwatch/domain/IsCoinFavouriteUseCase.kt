package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class IsCoinFavouriteUseCase @Inject constructor(
    private val favouriteCoinRepository: FavouriteCoinRepository
) {
    operator fun invoke(coinId: String): Flow<Result<Boolean>> {
        return isCoinFavourite(coinId = coinId)
    }

    private fun isCoinFavourite(coinId: String): Flow<Result<Boolean>> {
        return favouriteCoinRepository.isCoinFavourite(coinId = coinId)
    }
}

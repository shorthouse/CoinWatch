package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.repository.favouriteCoin.FavouriteCoinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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

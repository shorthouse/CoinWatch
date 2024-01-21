package dev.shorthouse.coinwatch.data.repository.favouriteCoin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import kotlinx.coroutines.flow.Flow

interface FavouriteCoinRepository {
    fun getFavouriteCoins(): Flow<Result<List<FavouriteCoin>>>
    fun isCoinFavourite(favouriteCoin: FavouriteCoin): Flow<Result<Boolean>>
    suspend fun toggleIsCoinFavourite(favouriteCoin: FavouriteCoin)
}

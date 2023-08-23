package dev.shorthouse.coinwatch.data.repository.favouriteCoin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import kotlinx.coroutines.flow.Flow

interface FavouriteCoinRepository {
    fun getFavouriteCoins(): Flow<Result<List<FavouriteCoin>>>
    fun isCoinFavourite(coinId: String): Flow<Result<Boolean>>
    suspend fun insertFavouriteCoin(favouriteCoin: FavouriteCoin)
    suspend fun deleteFavouriteCoin(favouriteCoin: FavouriteCoin)
}

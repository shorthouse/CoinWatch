package dev.shorthouse.coinwatch.data.repository.favouriteCoinId

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import kotlinx.coroutines.flow.Flow

interface FavouriteCoinIdRepository {
    fun getFavouriteCoinIds(): Flow<Result<List<FavouriteCoinId>>>
    fun isCoinFavourite(favouriteCoinId: FavouriteCoinId): Flow<Result<Boolean>>
    suspend fun toggleIsCoinFavourite(favouriteCoinId: FavouriteCoinId)
}

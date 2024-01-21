package dev.shorthouse.coinwatch.data.source.local

import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import kotlinx.coroutines.flow.Flow

interface CoinLocalDataSource {
    fun getCachedCoins(): Flow<List<CachedCoin>>
    suspend fun refreshCachedCoins(coins: List<CachedCoin>)
    fun getFavouriteCoinIds(): Flow<List<FavouriteCoinId>>
    fun isCoinFavourite(favouriteCoinId: FavouriteCoinId): Flow<Boolean>
    suspend fun toggleIsCoinFavourite(favouriteCoinId: FavouriteCoinId)
}

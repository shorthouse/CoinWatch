package dev.shorthouse.coinwatch.data.source.local

import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import kotlinx.coroutines.flow.Flow

interface CoinLocalDataSource {
    fun getCachedCoins(): Flow<List<CachedCoin>>
    suspend fun refreshCachedCoins(coins: List<CachedCoin>)
    fun getFavouriteCoins(): Flow<List<FavouriteCoin>>
    fun isCoinFavourite(coinId: String): Flow<Boolean>
    suspend fun toggleIsCoinFavourite(favouriteCoin: FavouriteCoin)
}

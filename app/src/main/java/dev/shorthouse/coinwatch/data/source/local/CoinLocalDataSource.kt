package dev.shorthouse.coinwatch.data.source.local

import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import kotlinx.coroutines.flow.Flow

interface CoinLocalDataSource {
    fun getCoins(): Flow<List<CachedCoin>>
    suspend fun updateCoins(coins: List<CachedCoin>)
    fun getFavouriteCoins(): Flow<List<FavouriteCoin>>
    suspend fun updateFavouriteCoins(favouriteCoins: List<FavouriteCoin>)
    fun getFavouriteCoinIds(): Flow<List<FavouriteCoinId>>
    fun isCoinFavourite(favouriteCoinId: FavouriteCoinId): Flow<Boolean>
    suspend fun toggleIsCoinFavourite(favouriteCoinId: FavouriteCoinId)
}

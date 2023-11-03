package dev.shorthouse.coinwatch.data.source.local

import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import kotlinx.coroutines.flow.Flow

class CoinLocalDataSource(
    private val favouriteCoinDao: FavouriteCoinDao,
    private val cachedCoinDao: CachedCoinDao
) {
    fun getFavouriteCoins(): Flow<List<FavouriteCoin>> {
        return favouriteCoinDao.getFavouriteCoins()
    }

    fun isCoinFavourite(coinId: String): Flow<Boolean> {
        return favouriteCoinDao.isCoinFavourite(coinId = coinId)
    }

    suspend fun insert(favouriteCoin: FavouriteCoin) {
        favouriteCoinDao.insert(favouriteCoin)
    }

    suspend fun delete(favouriteCoin: FavouriteCoin) {
        favouriteCoinDao.delete(favouriteCoin)
    }

    fun getCachedCoins(): Flow<List<CachedCoin>> {
        return cachedCoinDao.getCachedCoins()
    }

    suspend fun insertCachedCoins(coins: List<CachedCoin>) {
        cachedCoinDao.insertCachedCoins(coins)
    }

    suspend fun deleteAllCachedCoins() {
        cachedCoinDao.deleteAllCachedCoins()
    }
}

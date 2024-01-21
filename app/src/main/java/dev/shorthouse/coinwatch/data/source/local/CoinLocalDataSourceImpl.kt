package dev.shorthouse.coinwatch.data.source.local

import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import kotlinx.coroutines.flow.Flow

class CoinLocalDataSourceImpl(
    private val favouriteCoinDao: FavouriteCoinDao,
    private val cachedCoinDao: CachedCoinDao
) : CoinLocalDataSource {
    override fun getCachedCoins(): Flow<List<CachedCoin>> {
        return cachedCoinDao.getCachedCoins()
    }

    override suspend fun refreshCachedCoins(coins: List<CachedCoin>) {
        cachedCoinDao.refreshCachedCoins(coins)
    }

    override fun getFavouriteCoins(): Flow<List<FavouriteCoin>> {
        return favouriteCoinDao.getFavouriteCoins()
    }

    override fun isCoinFavourite(coinId: String): Flow<Boolean> {
        return favouriteCoinDao.isCoinFavourite(coinId = coinId)
    }

    override suspend fun toggleIsCoinFavourite(favouriteCoin: FavouriteCoin) {
        val isCoinFavourite = favouriteCoinDao.isCoinFavouriteOneShot(favouriteCoin.id)

        if (isCoinFavourite) {
            favouriteCoinDao.delete(favouriteCoin)
        } else {
            favouriteCoinDao.insert(favouriteCoin)
        }
    }

    override suspend fun insertFavouriteCoin(favouriteCoin: FavouriteCoin) {
        favouriteCoinDao.insert(favouriteCoin)
    }

    override suspend fun deleteFavouriteCoin(favouriteCoin: FavouriteCoin) {
        favouriteCoinDao.delete(favouriteCoin)
    }
}

package dev.shorthouse.coinwatch.data.source.local

import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
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

    override fun getFavouriteCoinIds(): Flow<List<FavouriteCoinId>> {
        return favouriteCoinDao.getFavouriteCoinIds()
    }

    override fun isCoinFavourite(favouriteCoinId: FavouriteCoinId): Flow<Boolean> {
        return favouriteCoinDao.isCoinFavourite(coinId = favouriteCoinId.id)
    }

    override suspend fun toggleIsCoinFavourite(favouriteCoinId: FavouriteCoinId) {
        val isCoinFavourite = favouriteCoinDao.isCoinFavouriteOneShot(favouriteCoinId.id)

        if (isCoinFavourite) {
            favouriteCoinDao.delete(favouriteCoinId)
        } else {
            favouriteCoinDao.insert(favouriteCoinId)
        }
    }
}

package dev.shorthouse.coinwatch.data.source.local.database

import dev.shorthouse.coinwatch.data.source.local.database.dao.CoinDao
import dev.shorthouse.coinwatch.data.source.local.database.dao.FavouriteCoinDao
import dev.shorthouse.coinwatch.data.source.local.database.dao.FavouriteCoinIdDao
import dev.shorthouse.coinwatch.data.source.local.database.model.Coin
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoinId
import kotlinx.coroutines.flow.Flow

class CoinLocalDataSourceImpl(
    private val coinDao: CoinDao,
    private val favouriteCoinDao: FavouriteCoinDao,
    private val favouriteCoinIdDao: FavouriteCoinIdDao
) : CoinLocalDataSource {
    override fun getCoins(): Flow<List<Coin>> {
        return coinDao.getCoins()
    }

    override suspend fun updateCoins(coins: List<Coin>) {
        coinDao.updateCoins(coins)
    }

    override fun getFavouriteCoins(): Flow<List<FavouriteCoin>> {
        return favouriteCoinDao.getFavouriteCoins()
    }

    override suspend fun updateFavouriteCoins(favouriteCoins: List<FavouriteCoin>) {
        favouriteCoinDao.updateFavouriteCoins(favouriteCoins)
    }

    override fun getFavouriteCoinIds(): Flow<List<FavouriteCoinId>> {
        return favouriteCoinIdDao.getFavouriteCoinIds()
    }

    override fun isCoinFavourite(favouriteCoinId: FavouriteCoinId): Flow<Boolean> {
        return favouriteCoinIdDao.isCoinFavourite(coinId = favouriteCoinId.id)
    }

    override suspend fun toggleIsCoinFavourite(favouriteCoinId: FavouriteCoinId) {
        val isCoinFavourite = favouriteCoinIdDao.isCoinFavouriteOneShot(favouriteCoinId.id)

        if (isCoinFavourite) {
            favouriteCoinIdDao.delete(favouriteCoinId)
        } else {
            favouriteCoinIdDao.insert(favouriteCoinId)
        }
    }
}

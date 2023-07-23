package dev.shorthouse.cryptodata.data.source.local

import dev.shorthouse.cryptodata.data.source.local.model.FavouriteCoin
import kotlinx.coroutines.flow.Flow

class CoinLocalDataSource(private val favouriteCoinDao: FavouriteCoinDao) {
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
}

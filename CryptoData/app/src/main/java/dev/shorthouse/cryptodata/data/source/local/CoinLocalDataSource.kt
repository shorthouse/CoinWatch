package dev.shorthouse.cryptodata.data.source.local

import dev.shorthouse.cryptodata.data.source.local.model.FavouriteCoin

class CoinLocalDataSource(private val favouriteCoinDao: FavouriteCoinDao) {
    suspend fun getAllFavouriteCoins(): List<FavouriteCoin> {
        return favouriteCoinDao.getFavouriteCoins()
    }

    suspend fun insert(favouriteCoin: FavouriteCoin) {
        favouriteCoinDao.insert(favouriteCoin)
    }

    suspend fun delete(favouriteCoin: FavouriteCoin) {
        favouriteCoinDao.delete(favouriteCoin)
    }
}

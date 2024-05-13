package dev.shorthouse.coinwatch.data.repository.favouriteCoin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoin
import kotlinx.coroutines.flow.Flow

interface FavouriteCoinRepository {
    suspend fun getRemoteFavouriteCoins(
        coinIds: List<String>,
        coinSort: CoinSort,
        currency: Currency
    ): Result<List<FavouriteCoin>>

    fun getCachedFavouriteCoins(): Flow<Result<List<FavouriteCoin>>>
    suspend fun updateCachedFavouriteCoins(favouriteCoins: List<FavouriteCoin>)
}

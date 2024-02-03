package dev.shorthouse.coinwatch.data.repository.favouriteCoin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
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

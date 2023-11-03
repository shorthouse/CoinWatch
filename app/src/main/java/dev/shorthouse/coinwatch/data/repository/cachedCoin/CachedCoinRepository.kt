package dev.shorthouse.coinwatch.data.repository.cachedCoin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.data.datastore.Currency
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.model.Coin
import kotlinx.coroutines.flow.Flow

interface CachedCoinRepository {
    suspend fun getRemoteCoins(
        coinIds: List<String> = emptyList(),
        coinSort: CoinSort,
        currency: Currency
    ): Result<List<CachedCoin>>

    fun getCachedCoins(): Flow<Result<List<Coin>>>
    suspend fun insertCachedCoins(cachedCoins: List<CachedCoin>)
    suspend fun deleteAllCachedCoins()
}

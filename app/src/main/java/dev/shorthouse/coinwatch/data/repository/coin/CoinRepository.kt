package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getRemoteCoins(coinSort: CoinSort, currency: Currency): Result<List<CachedCoin>>
    fun getCachedCoins(): Flow<Result<List<CachedCoin>>>
    suspend fun updateCachedCoins(coins: List<CachedCoin>)
}

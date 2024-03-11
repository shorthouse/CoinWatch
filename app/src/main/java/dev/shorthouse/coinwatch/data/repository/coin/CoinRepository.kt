package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.Coin
import dev.shorthouse.coinwatch.data.preferences.global.CoinSort
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getRemoteCoins(coinSort: CoinSort, currency: Currency): Result<List<Coin>>
    fun getCachedCoins(): Flow<Result<List<Coin>>>
    suspend fun updateCachedCoins(coins: List<Coin>)
}

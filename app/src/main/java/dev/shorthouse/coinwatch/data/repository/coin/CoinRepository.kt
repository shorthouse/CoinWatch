package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.datastore.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.local.database.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getRemoteCoins(
        coinSort: CoinSort,
        currency: Currency,
    ): Result<List<Coin>>

    fun getCachedCoins(): Flow<Result<List<Coin>>>
    suspend fun updateCachedCoins(coins: List<Coin>)
}

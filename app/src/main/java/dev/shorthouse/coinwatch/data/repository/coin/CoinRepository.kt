package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.data.preferences.market.MarketCoinSort
import dev.shorthouse.coinwatch.data.source.local.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getRemoteCoins(
        marketCoinSort: MarketCoinSort,
        currency: Currency
    ): Result<List<Coin>>
    fun getCachedCoins(): Flow<Result<List<Coin>>>
    suspend fun updateCachedCoins(coins: List<Coin>)
}

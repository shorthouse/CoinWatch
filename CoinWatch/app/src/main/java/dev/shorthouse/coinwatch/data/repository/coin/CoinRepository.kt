package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(): Flow<Result<List<Coin>>>
}

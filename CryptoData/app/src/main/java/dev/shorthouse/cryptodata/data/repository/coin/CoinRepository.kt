package dev.shorthouse.cryptodata.data.repository.coin

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(): Flow<Result<List<Coin>>>
}

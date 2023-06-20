package dev.shorthouse.cryptodata.data.repository

import dev.shorthouse.cryptodata.common.Resource
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.CoinDetail
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getCryptocurrencies(): Flow<Resource<List<Coin>>>

    suspend fun getCoinDetail(): Flow<Resource<CoinDetail>>
}

package dev.shorthouse.cryptodata.data.repository

import dev.shorthouse.cryptodata.common.Resource
import dev.shorthouse.cryptodata.model.Cryptocurrency
import kotlinx.coroutines.flow.Flow

interface CryptocurrencyRepository {
    suspend fun getCryptocurrencies(): Flow<Resource<List<Cryptocurrency>>>
}

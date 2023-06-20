package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Resource
import dev.shorthouse.cryptodata.data.repository.CoinRepository
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.Coin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCryptocurrenciesUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Flow<Resource<List<Coin>>> {
        return getCryptocurrencies()
    }

    private suspend fun getCryptocurrencies(): Flow<Resource<List<Coin>>> {
        return coinRepository.getCryptocurrencies()
            .flowOn(ioDispatcher)
    }
}

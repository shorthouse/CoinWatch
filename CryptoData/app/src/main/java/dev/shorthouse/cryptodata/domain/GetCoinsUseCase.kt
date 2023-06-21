package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Resource
import dev.shorthouse.cryptodata.data.repository.CoinRepository
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.Coin
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<Resource<List<Coin>>> {
        return getCryptocurrencies()
    }

    private fun getCryptocurrencies(): Flow<Resource<List<Coin>>> {
        return coinRepository.getCryptocurrencies()
            .flowOn(ioDispatcher)
    }
}

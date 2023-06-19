package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Resource
import dev.shorthouse.cryptodata.data.repository.CryptocurrencyRepository
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.Cryptocurrency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCryptocurrenciesUseCase @Inject constructor(
    private val cryptocurrencyRepository: CryptocurrencyRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Flow<Resource<List<Cryptocurrency>>> {
        return getCryptocurrencies()
    }

    private suspend fun getCryptocurrencies(): Flow<Resource<List<Cryptocurrency>>> {
        return cryptocurrencyRepository.getCryptocurrencies()
            .flowOn(ioDispatcher)
    }
}

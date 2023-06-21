package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Resource
import dev.shorthouse.cryptodata.data.repository.CoinRepository
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.CoinDetail
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetCoinDetailUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<Resource<CoinDetail>> {
        return getCoinDetail()
    }

    private fun getCoinDetail(): Flow<Resource<CoinDetail>> {
        return coinRepository.getCoinDetail()
            .flowOn(ioDispatcher)
    }
}

package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Result
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
    operator fun invoke(): Flow<Result<List<Coin>>> {
        return getCoins()
    }

    private fun getCoins(): Flow<Result<List<Coin>>> {
        return coinRepository.getCoins()
            .flowOn(ioDispatcher)
    }
}

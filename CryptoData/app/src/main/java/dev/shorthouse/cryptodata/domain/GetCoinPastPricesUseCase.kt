package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.repository.CoinRepository
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.CoinPastPrices
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetCoinPastPricesUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(coinId: String, periodDays: String): Flow<Result<CoinPastPrices>> {
        return getCoinPastPrices(coinId = coinId, periodDays = periodDays)
    }

    private fun getCoinPastPrices(
        coinId: String,
        periodDays: String
    ): Flow<Result<CoinPastPrices>> {
        return coinRepository.getCoinPastPrices(coinId = coinId, periodDays = periodDays)
            .flowOn(ioDispatcher)
    }
}

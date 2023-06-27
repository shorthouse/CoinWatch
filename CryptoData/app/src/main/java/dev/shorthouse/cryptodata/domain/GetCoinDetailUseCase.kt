package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.repository.detail.CoinDetailRepository
import dev.shorthouse.cryptodata.model.CoinDetail
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCoinDetailUseCase @Inject constructor(
    private val coinDetailRepository: CoinDetailRepository
) {
    operator fun invoke(coinId: String, periodDays: String): Flow<Result<CoinDetail>> {
        return getCoinDetail(
            coinId = coinId,
            periodDays = periodDays
        )
    }

    private fun getCoinDetail(coinId: String, periodDays: String): Flow<Result<CoinDetail>> {
        return coinDetailRepository.getCoinDetail(
            coinId = coinId,
            periodDays = periodDays
        )
    }
}

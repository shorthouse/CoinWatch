package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.detail.CoinDetailRepository
import dev.shorthouse.coinwatch.model.CoinDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinDetailUseCase @Inject constructor(
    private val coinDetailRepository: CoinDetailRepository
) {
    operator fun invoke(coinId: String): Flow<Result<CoinDetail>> {
        return getCoinDetail(coinId = coinId)
    }

    private fun getCoinDetail(coinId: String): Flow<Result<CoinDetail>> {
        return coinDetailRepository.getCoinDetail(coinId = coinId)
    }
}

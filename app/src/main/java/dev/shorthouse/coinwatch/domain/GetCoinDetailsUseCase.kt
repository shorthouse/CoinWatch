package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.details.CoinDetailsRepository
import dev.shorthouse.coinwatch.model.CoinDetails
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCoinDetailsUseCase @Inject constructor(
    private val coinDetailsRepository: CoinDetailsRepository
) {
    operator fun invoke(coinId: String): Flow<Result<CoinDetails>> {
        return getCoinDetails(coinId = coinId)
    }

    private fun getCoinDetails(coinId: String): Flow<Result<CoinDetails>> {
        return coinDetailsRepository.getCoinDetails(coinId = coinId)
    }
}

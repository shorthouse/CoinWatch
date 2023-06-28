package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.repository.coin.CoinRepository
import dev.shorthouse.cryptodata.model.Coin
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {
    operator fun invoke(coinId: String? = null): Flow<Result<List<Coin>>> {
        return getCoins(coinId = coinId)
    }

    private fun getCoins(coinId: String? = null): Flow<Result<List<Coin>>> {
        return coinRepository.getCoins(coinId = coinId)
    }
}

package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.model.Coin
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {
    operator fun invoke(): Flow<Result<List<Coin>>> {
        return getCoins()
    }

    private fun getCoins(): Flow<Result<List<Coin>>> {
        return coinRepository.getCoins()
    }
}

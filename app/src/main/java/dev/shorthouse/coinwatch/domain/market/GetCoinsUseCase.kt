package dev.shorthouse.coinwatch.domain.market

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.data.source.local.database.model.Coin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {
    operator fun invoke(): Flow<Result<List<Coin>>> {
        return getCoins()
    }

    private fun getCoins(): Flow<Result<List<Coin>>> {
        return coinRepository.getCachedCoins()
    }
}

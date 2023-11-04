package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.cachedCoin.CachedCoinRepository
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCachedCoinsUseCase @Inject constructor(
    private val cachedCoinRepository: CachedCoinRepository
) {
    operator fun invoke(): Flow<Result<List<CachedCoin>>> {
        return getCoins()
    }

    private fun getCoins(): Flow<Result<List<CachedCoin>>> {
        return cachedCoinRepository.getCachedCoins()
    }
}

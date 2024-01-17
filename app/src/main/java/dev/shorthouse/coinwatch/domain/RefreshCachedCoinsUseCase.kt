package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.cachedCoin.CachedCoinRepository
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import javax.inject.Inject

class RefreshCachedCoinsUseCase @Inject constructor(
    private val cachedCoinRepository: CachedCoinRepository
) {
    suspend operator fun invoke(coinSort: CoinSort, currency: Currency): Result<List<CachedCoin>> {
        return refreshCachedCoins(coinSort = coinSort, currency = currency)
    }

    private suspend fun refreshCachedCoins(
        coinSort: CoinSort,
        currency: Currency
    ): Result<List<CachedCoin>> {
        val remoteCoinsResult = cachedCoinRepository.getRemoteCoins(
            coinSort = coinSort,
            currency = currency
        )

        if (remoteCoinsResult is Result.Success) {
            cachedCoinRepository.refreshCachedCoins(remoteCoinsResult.data)
        }

        return remoteCoinsResult
    }
}

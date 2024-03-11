package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.data.source.local.model.Coin
import dev.shorthouse.coinwatch.data.preferences.global.CoinSort
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import javax.inject.Inject

class UpdateCachedCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(coinSort: CoinSort, currency: Currency): Result<List<Coin>> {
        return refreshCachedCoins(coinSort = coinSort, currency = currency)
    }

    private suspend fun refreshCachedCoins(
        coinSort: CoinSort,
        currency: Currency
    ): Result<List<Coin>> {
        val remoteCoinsResult = coinRepository.getRemoteCoins(
            coinSort = coinSort,
            currency = currency
        )

        if (remoteCoinsResult is Result.Success) {
            val coins = remoteCoinsResult.data
            coinRepository.updateCachedCoins(coins = coins)
        }

        return remoteCoinsResult
    }
}

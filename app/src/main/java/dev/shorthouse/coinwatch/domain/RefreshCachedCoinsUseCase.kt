package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.UserPreferencesRepository
import dev.shorthouse.coinwatch.data.repository.cachedCoin.CachedCoinRepository
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class RefreshCachedCoinsUseCase @Inject constructor(
    private val cachedCoinRepository: CachedCoinRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Result<List<CachedCoin>> {
        return refreshCachedCoins()
    }

    private suspend fun refreshCachedCoins(): Result<List<CachedCoin>> {
        val userPreferences = userPreferencesRepository.userPreferencesFlow.first()

        val remoteCoinsResult = cachedCoinRepository.getRemoteCoins(
            coinSort = userPreferences.coinSort,
            currency = userPreferences.currency
        )

        if (remoteCoinsResult is Result.Success) {
            cachedCoinRepository.deleteAllCachedCoins()
            cachedCoinRepository.insertCachedCoins(remoteCoinsResult.data)
        }

        return remoteCoinsResult
    }
}

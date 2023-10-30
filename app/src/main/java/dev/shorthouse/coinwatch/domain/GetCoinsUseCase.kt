package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.UserPreferencesRepository
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.model.Coin
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class GetCoinsUseCase @Inject constructor(
    private val coinRepository: CoinRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Result<List<Coin>>> {
        return getCoins()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCoins(): Flow<Result<List<Coin>>> {
        return userPreferencesRepository.userPreferencesFlow.flatMapLatest { userPreferences ->
            coinRepository.getCoins(coinSort = userPreferences.coinSort)
        }
    }
}

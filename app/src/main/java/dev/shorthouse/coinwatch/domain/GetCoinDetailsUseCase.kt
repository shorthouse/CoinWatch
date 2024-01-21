package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.details.CoinDetailsRepository
import dev.shorthouse.coinwatch.data.userPreferences.UserPreferencesRepository
import dev.shorthouse.coinwatch.model.CoinDetails
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class GetCoinDetailsUseCase @Inject constructor(
    private val coinDetailsRepository: CoinDetailsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(coinId: String): Flow<Result<CoinDetails>> {
        return getCoinDetails(coinId = coinId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCoinDetails(coinId: String): Flow<Result<CoinDetails>> {
        return userPreferencesRepository.userPreferencesFlow.flatMapLatest { userPreferences ->
            coinDetailsRepository.getCoinDetails(
                coinId = coinId,
                currency = userPreferences.currency
            )
        }
    }
}

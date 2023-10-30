package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.data.datastore.UserPreferencesRepository
import javax.inject.Inject

class UpdateCoinSortUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(coinSort: CoinSort) {
        updateCoinSort(coinSort = coinSort)
    }

    private suspend fun updateCoinSort(coinSort: CoinSort) {
        userPreferencesRepository.updateCoinSort(coinSort = coinSort)
    }
}

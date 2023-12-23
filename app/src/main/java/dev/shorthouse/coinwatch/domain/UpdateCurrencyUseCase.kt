package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.data.userPreferences.UserPreferencesRepository
import javax.inject.Inject

class UpdateCurrencyUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(currency: Currency) {
        updateCoinSort(currency = currency)
    }

    private suspend fun updateCoinSort(currency: Currency) {
        userPreferencesRepository.updateCurrency(currency = currency)
    }
}

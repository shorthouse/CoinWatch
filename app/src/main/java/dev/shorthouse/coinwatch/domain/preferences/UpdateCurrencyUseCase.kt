package dev.shorthouse.coinwatch.domain.preferences

import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.data.source.local.preferences.global.UserPreferencesRepository
import javax.inject.Inject

class UpdateCurrencyUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(currency: Currency) {
        updateCurrency(currency = currency)
    }

    private suspend fun updateCurrency(currency: Currency) {
        userPreferencesRepository.updateCurrency(currency = currency)
    }
}

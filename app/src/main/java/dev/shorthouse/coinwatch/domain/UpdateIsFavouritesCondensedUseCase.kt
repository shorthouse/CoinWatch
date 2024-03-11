package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.preferences.global.UserPreferencesRepository
import javax.inject.Inject

class UpdateIsFavouritesCondensedUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(isCondensed: Boolean) {
        userPreferencesRepository.updateIsFavouritesCondensed(isCondensed = isCondensed)
    }

    private suspend fun updateIsFavouritesCondensed(isCondensed: Boolean) {
        userPreferencesRepository.updateIsFavouritesCondensed(isCondensed = isCondensed)
    }
}

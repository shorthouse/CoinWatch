package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.userPreferences.StartDestination
import dev.shorthouse.coinwatch.data.userPreferences.UserPreferencesRepository
import javax.inject.Inject

class UpdateStartDestinationUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(startDestination: StartDestination) {
        updateStartDestination(startDestination = startDestination)
    }

    private suspend fun updateStartDestination(startDestination: StartDestination) {
        userPreferencesRepository.updateStartDestination(startDestination = startDestination)
    }
}

package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.userPreferences.StartScreen
import dev.shorthouse.coinwatch.data.userPreferences.UserPreferencesRepository
import javax.inject.Inject

class UpdateStartDestinationUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(startScreen: StartScreen) {
        updateStartDestination(startScreen = startScreen)
    }

    private suspend fun updateStartDestination(startScreen: StartScreen) {
        userPreferencesRepository.updateStartScreen(startScreen = startScreen)
    }
}

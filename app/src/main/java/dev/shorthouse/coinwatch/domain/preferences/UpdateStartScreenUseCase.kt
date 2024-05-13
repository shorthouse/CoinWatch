package dev.shorthouse.coinwatch.domain.preferences

import dev.shorthouse.coinwatch.data.source.local.preferences.global.StartScreen
import dev.shorthouse.coinwatch.data.source.local.preferences.global.UserPreferencesRepository
import javax.inject.Inject

class UpdateStartScreenUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(startScreen: StartScreen) {
        updateStartScreen(startScreen = startScreen)
    }

    private suspend fun updateStartScreen(startScreen: StartScreen) {
        userPreferencesRepository.updateStartScreen(startScreen = startScreen)
    }
}

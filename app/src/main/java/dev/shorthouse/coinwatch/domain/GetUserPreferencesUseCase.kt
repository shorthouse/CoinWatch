package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.preferences.global.UserPreferences
import dev.shorthouse.coinwatch.data.preferences.global.UserPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<UserPreferences> {
        return getUserPreferences()
    }

    private fun getUserPreferences(): Flow<UserPreferences> {
        return userPreferencesRepository.userPreferencesFlow
    }
}

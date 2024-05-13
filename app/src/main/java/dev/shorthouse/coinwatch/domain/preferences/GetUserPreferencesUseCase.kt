package dev.shorthouse.coinwatch.domain.preferences

import dev.shorthouse.coinwatch.data.source.local.preferences.global.UserPreferences
import dev.shorthouse.coinwatch.data.source.local.preferences.global.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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

package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.datastore.UserPreferences
import dev.shorthouse.coinwatch.data.datastore.UserPreferencesRepository
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

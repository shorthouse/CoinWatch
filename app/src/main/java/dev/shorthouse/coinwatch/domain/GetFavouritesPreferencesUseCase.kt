package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.preferences.favourites.FavouritesPreferences
import dev.shorthouse.coinwatch.data.preferences.favourites.FavouritesPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouritesPreferencesUseCase @Inject constructor(
    private val favouritesPreferencesRepository: FavouritesPreferencesRepository
) {
    operator fun invoke(): Flow<FavouritesPreferences> {
        return getFavouritesPreferences()
    }

    private fun getFavouritesPreferences(): Flow<FavouritesPreferences> {
        return favouritesPreferencesRepository.favouritesPreferencesFlow
    }
}

package dev.shorthouse.coinwatch.domain.preferences

import dev.shorthouse.coinwatch.data.source.local.preferences.favourites.FavouritesPreferencesRepository
import javax.inject.Inject

class UpdateIsFavouritesCondensedUseCase @Inject constructor(
    private val favouritesPreferencesRepository: FavouritesPreferencesRepository
) {
    suspend operator fun invoke(isCondensed: Boolean) {
        updateIsFavouritesCondensed(isCondensed = isCondensed)
    }

    private suspend fun updateIsFavouritesCondensed(isCondensed: Boolean) {
        favouritesPreferencesRepository.updateIsFavouritesCondensed(isCondensed = isCondensed)
    }
}

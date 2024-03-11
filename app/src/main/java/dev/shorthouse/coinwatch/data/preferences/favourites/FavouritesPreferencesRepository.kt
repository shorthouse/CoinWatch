package dev.shorthouse.coinwatch.data.preferences.favourites

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class FavouritesPreferencesRepository @Inject constructor(
    private val favouritesPreferencesDataStore: DataStore<FavouritesPreferences>
) {
    val favouritesPreferencesFlow: Flow<FavouritesPreferences> = favouritesPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e("Error reading favourites preferences", exception)
                emit(FavouritesPreferences())
            } else {
                throw exception
            }
        }

    suspend fun updateIsFavouritesCondensed(isCondensed: Boolean) {
        if (isCondensed != favouritesPreferencesFlow.first().isFavouritesCondensed) {
            favouritesPreferencesDataStore.updateData { currentPreferences ->
                currentPreferences.copy(isFavouritesCondensed = isCondensed)
            }
        }
    }
}

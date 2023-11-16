package dev.shorthouse.coinwatch.data.datastore

import androidx.datastore.core.DataStore
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import timber.log.Timber

class UserPreferencesRepository @Inject constructor(
    private val userPreferencesDataStore: DataStore<UserPreferences>
) {
    val userPreferencesFlow: Flow<UserPreferences> = userPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e("Error reading user preferences", exception)
                emit(UserPreferences())
            } else {
                throw exception
            }
        }

    suspend fun updateCurrency(currency: Currency) {
        if (currency != userPreferencesFlow.first().currency) {
            userPreferencesDataStore.updateData { currentPreferences ->
                currentPreferences.copy(currency = currency)
            }
        }
    }

    suspend fun updateCoinSort(coinSort: CoinSort) {
        if (coinSort != userPreferencesFlow.first().coinSort) {
            userPreferencesDataStore.updateData { currentPreferences ->
                currentPreferences.copy(coinSort = coinSort)
            }
        }
    }
}

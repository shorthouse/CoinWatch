package dev.shorthouse.coinwatch.data.preferences.market

import androidx.datastore.core.DataStore
import dev.shorthouse.coinwatch.data.preferences.common.CoinSort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class MarketPreferencesRepository @Inject constructor(
    private val marketPreferencesDataStore: DataStore<MarketPreferences>
) {
    val marketPreferencesFlow: Flow<MarketPreferences> = marketPreferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading market preferences", exception)
                emit(MarketPreferences())
            } else {
                throw exception
            }
        }

    suspend fun updateCoinSort(coinSort: CoinSort) {
        if (coinSort != marketPreferencesFlow.first().coinSort) {
            marketPreferencesDataStore.updateData { currentPreferences ->
                currentPreferences.copy(coinSort = coinSort)
            }
        }
    }
}

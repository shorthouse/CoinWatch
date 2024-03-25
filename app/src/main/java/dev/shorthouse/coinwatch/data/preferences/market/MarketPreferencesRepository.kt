package dev.shorthouse.coinwatch.data.preferences.market

import androidx.datastore.core.DataStore
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

    suspend fun updateMarketCoinSort(marketCoinSort: MarketCoinSort) {
        if (marketCoinSort != marketPreferencesFlow.first().marketCoinSort) {
            marketPreferencesDataStore.updateData { currentPreferences ->
                currentPreferences.copy(marketCoinSort = marketCoinSort)
            }
        }
    }
}

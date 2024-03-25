package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.preferences.market.MarketPreferences
import dev.shorthouse.coinwatch.data.preferences.market.MarketPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMarketPreferencesUseCase @Inject constructor(
    private val marketPreferencesRepository: MarketPreferencesRepository
) {
    operator fun invoke(): Flow<MarketPreferences> {
        return getMarketPreferences()
    }

    private fun getMarketPreferences(): Flow<MarketPreferences> {
        return marketPreferencesRepository.marketPreferencesFlow
    }
}

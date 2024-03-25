package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.data.preferences.market.MarketCoinSort
import dev.shorthouse.coinwatch.data.preferences.market.MarketPreferencesRepository
import javax.inject.Inject

class UpdateMarketCoinSortUseCase @Inject constructor(
    private val marketPreferencesRepository: MarketPreferencesRepository
) {
    suspend operator fun invoke(marketCoinSort: MarketCoinSort) {
        updateMarketCoinSort(marketCoinSort = marketCoinSort)
    }
    
    private suspend fun updateMarketCoinSort(marketCoinSort: MarketCoinSort) {
        marketPreferencesRepository.updateMarketCoinSort(marketCoinSort = marketCoinSort)
    }
}

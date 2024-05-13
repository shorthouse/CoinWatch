package dev.shorthouse.coinwatch.domain.preferences

import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.preferences.market.MarketPreferencesRepository
import javax.inject.Inject

class UpdateMarketCoinSortUseCase @Inject constructor(
    private val marketPreferencesRepository: MarketPreferencesRepository
) {
    suspend operator fun invoke(coinSort: CoinSort) {
        updateMarketCoinSort(coinSort = coinSort)
    }

    private suspend fun updateMarketCoinSort(coinSort: CoinSort) {
        marketPreferencesRepository.updateCoinSort(coinSort = coinSort)
    }
}

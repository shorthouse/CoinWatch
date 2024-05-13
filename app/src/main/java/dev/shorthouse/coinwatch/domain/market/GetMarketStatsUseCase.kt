package dev.shorthouse.coinwatch.domain.market

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.marketStats.MarketStatsRepository
import dev.shorthouse.coinwatch.model.MarketStats
import javax.inject.Inject

class GetMarketStatsUseCase @Inject constructor(
    private val marketStatsRepository: MarketStatsRepository
) {
    suspend operator fun invoke(): Result<MarketStats> {
        return getMarketStats()
    }

    private suspend fun getMarketStats(): Result<MarketStats> {
        return marketStatsRepository.getMarketStats()
    }
}

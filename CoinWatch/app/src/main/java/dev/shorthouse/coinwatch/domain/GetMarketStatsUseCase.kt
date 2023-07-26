package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.marketStats.MarketStatsRepository
import dev.shorthouse.coinwatch.model.MarketStats
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMarketStatsUseCase @Inject constructor(
    private val marketStatsRepository: MarketStatsRepository
) {
    operator fun invoke(): Flow<Result<MarketStats>> {
        return getMarketStats()
    }

    private fun getMarketStats(): Flow<Result<MarketStats>> {
        return marketStatsRepository.getMarketStats()
    }
}

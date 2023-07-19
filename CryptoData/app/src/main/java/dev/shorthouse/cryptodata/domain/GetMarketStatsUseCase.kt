package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.repository.marketStats.MarketStatsRepository
import dev.shorthouse.cryptodata.model.MarketStats
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

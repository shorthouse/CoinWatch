package dev.shorthouse.coinwatch.data.repository.marketStats

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.model.MarketStats

interface MarketStatsRepository {
    suspend fun getMarketStats(): Result<MarketStats>
}

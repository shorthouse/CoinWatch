package dev.shorthouse.coinwatch.data.repository.marketStats

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.model.MarketStats
import kotlinx.coroutines.flow.Flow

interface MarketStatsRepository {
    fun getMarketStats(): Flow<Result<MarketStats>>
}

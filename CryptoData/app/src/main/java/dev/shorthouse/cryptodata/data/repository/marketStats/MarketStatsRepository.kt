package dev.shorthouse.cryptodata.data.repository.marketStats

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.model.MarketStats
import kotlinx.coroutines.flow.Flow

interface MarketStatsRepository {
    fun getMarketStats(): Flow<Result<MarketStats>>
}

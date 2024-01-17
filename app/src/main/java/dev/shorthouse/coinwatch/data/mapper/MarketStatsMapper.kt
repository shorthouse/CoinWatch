package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import dev.shorthouse.coinwatch.model.MarketStats
import dev.shorthouse.coinwatch.model.Percentage
import javax.inject.Inject

class MarketStatsMapper @Inject constructor() {
    fun mapApiModelToModel(apiModel: MarketStatsApiModel): MarketStats {
        val marketStats = apiModel.marketStatsDataHolder?.marketStatsData

        return MarketStats(
            marketCapChangePercentage24h = Percentage(marketStats?.marketCapChangePercentage24h)
        )
    }
}

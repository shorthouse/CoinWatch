package dev.shorthouse.coinwatch.data.repository.marketStats

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.MarketStatsMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.model.MarketStats
import javax.inject.Inject
import timber.log.Timber

class MarketStatsRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val marketStatsMapper: MarketStatsMapper
) : MarketStatsRepository {
    override suspend fun getMarketStats(): Result<MarketStats> {
        return try {
            val response = coinNetworkDataSource.getMarketStats()
            val body = response.body()

            if (response.isSuccessful && body?.marketStatsDataHolder?.marketStatsData != null) {
                val marketStats = marketStatsMapper.mapApiModelToModel(body)
                Result.Success(marketStats)
            } else {
                Timber.e(
                    "getMarketStats unsuccessful retrofit response ${response.message()}"
                )
                Result.Error("Unable to fetch market stats")
            }
        } catch (e: Exception) {
            Timber.e("getMarketStats error ${e.message}")
            Result.Error("Unable to fetch market stats")
        }
    }
}

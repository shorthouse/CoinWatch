package dev.shorthouse.coinwatch.data.repository.globalmarket

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.GlobalMarketMapper
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.model.GlobalMarket
import timber.log.Timber
import javax.inject.Inject

class GlobalMarketRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val globalMarketMapper: GlobalMarketMapper,
) : GlobalMarketRepository {
    override suspend fun getGlobalMarket(currency: Currency): Result<GlobalMarket> {
        return try {
            val globalStatsResponse = coinNetworkDataSource.getGlobalStats(currency = currency)
            val globalMarketCoinStatsResponse = coinNetworkDataSource.getGlobalMarketCoinStats(
                currency = currency
            )

            val globalStatsBody = globalStatsResponse.body()
            val globalMarketCoinStatsBody = globalMarketCoinStatsResponse.body()

            val globalMarket = if (globalStatsBody != null && globalMarketCoinStatsBody != null) {
                globalMarketMapper.mapApiModelsToModel(
                    globalStatsApiModel = globalStatsBody,
                    globalMarketCoinStatsApiModel = globalMarketCoinStatsBody,
                    currency = currency
                )
            } else {
                null
            }

            if (
                globalStatsResponse.isSuccessful &&
                globalMarketCoinStatsResponse.isSuccessful &&
                globalMarket != null
            ) {
                Result.Success(globalMarket)
            } else {
                Timber.e("getGlobalMarket unsuccessful retrofit response")
                Result.Error(ERROR_MESSAGE)
            }
        } catch (e: Exception) {
            Timber.e("getGlobalMarket error ${e.message}")
            Result.Error(ERROR_MESSAGE)
        }
    }

    private companion object {
        const val ERROR_MESSAGE = "Unable to fetch global market"
    }
}

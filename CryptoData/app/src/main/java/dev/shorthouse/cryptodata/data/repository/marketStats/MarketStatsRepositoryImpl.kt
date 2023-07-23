package dev.shorthouse.cryptodata.data.repository.marketStats

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.cryptodata.data.source.remote.model.MarketStatsApiModel
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.MarketStats
import dev.shorthouse.cryptodata.model.Percentage
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class MarketStatsRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MarketStatsRepository {
    override fun getMarketStats(): Flow<Result<MarketStats>> = flow {
        emit(
            try {
                val response = coinNetworkDataSource.getMarketStats()
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    Result.Success(body.toMarketStats())
                } else {
                    Timber.e("getMarketStats unsuccessful retrofit response ${response.message()}")
                    Result.Error(message = "Unable to fetch market status")
                }
            } catch (e: Throwable) {
                Timber.e("getMarketStats error $e")
                Result.Error(message = "Unable to fetch market status")
            }
        )
    }.flowOn(ioDispatcher)

    private fun MarketStatsApiModel.toMarketStats(): MarketStats {
        return MarketStats(
            marketCapChangePercentage24h = Percentage(globalMarketData.marketCapChangePercentage24h)
        )
    }
}

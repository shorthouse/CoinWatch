package dev.shorthouse.coinwatch.data.repository.marketStats

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.MarketStats
import dev.shorthouse.coinwatch.model.Percentage
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class MarketStatsRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MarketStatsRepository {
    override fun getMarketStats(): Flow<Result<MarketStats>> = flow {
        val response = coinNetworkDataSource.getMarketStats()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            emit(Result.Success(body.toMarketStats()))
        } else {
            Timber.e("getMarketStats unsuccessful retrofit response ${response.message()}")
            emit(Result.Error("Unable to fetch market stats"))
        }
    }
        .catch { e ->
            Timber.e("getMarketStats error $e")
            emit(Result.Error("Unable to fetch market stats"))
        }.flowOn(ioDispatcher)

    private fun MarketStatsApiModel.toMarketStats(): MarketStats {
        return MarketStats(
            marketCapChangePercentage24h = Percentage(globalMarketData.marketCapChangePercentage24h)
        )
    }
}

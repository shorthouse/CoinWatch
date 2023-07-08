package dev.shorthouse.cryptodata.data.repository.chart

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.cryptodata.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.CoinChart
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class CoinChartRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinChartRepository {

    override fun getCoinChart(
        coinId: String,
        chartPeriodDays: String
    ): Flow<Result<CoinChart>> = flow {
        emit(
            try {
                val response = coinNetworkDataSource.getCoinChart(
                    coinId = coinId,
                    chartPeriodDays = chartPeriodDays
                )
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    Result.Success(body.toCoinChart())
                } else {
                    Timber.e("getCoinChart unsuccessful retrofit response ${response.message()}")
                    Result.Error(message = response.message())
                }
            } catch (e: Throwable) {
                Timber.e("getCoinChart error $e")
                Result.Error(message = e.message)
            }
        )
    }.flowOn(ioDispatcher)

    private fun CoinChartApiModel.toCoinChart(): CoinChart {
        val prices = this.prices.map { it.last() }
        val currentPrice = prices.last()

        val minPrice = prices.min()
        val minPriceChangePercentage = ((currentPrice - minPrice) / minPrice) * 100

        val maxPrice = prices.max()
        val maxPriceChangePercentage = ((currentPrice - maxPrice) / maxPrice) * 100

        val oldestPastPrice = prices.first()
        val periodPriceChangePercentage = ((currentPrice - oldestPastPrice) / oldestPastPrice) * 100

        return CoinChart(
            prices = prices,
            minPrice = Price(minPrice),
            minPriceChangePercentage = Percentage(minPriceChangePercentage),
            maxPrice = Price(maxPrice),
            maxPriceChangePercentage = Percentage(maxPriceChangePercentage),
            periodPriceChangePercentage = Percentage(periodPriceChangePercentage)
        )
    }
}

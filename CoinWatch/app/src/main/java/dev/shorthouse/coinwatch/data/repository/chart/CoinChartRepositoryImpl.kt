package dev.shorthouse.coinwatch.data.repository.chart

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class CoinChartRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinChartRepository {
    override fun getCoinChart(coinId: String, chartPeriod: String): Flow<Result<CoinChart>> = flow {
        val response = coinNetworkDataSource.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod
        )
        val body = response.body()

        if (response.isSuccessful && body != null) {
            emit(Result.Success(body.toCoinChart()))
        } else {
            Timber.e("getCoinChart unsuccessful retrofit response ${response.message()}")
            emit(Result.Error("Unable to fetch coin chart"))
        }
    }.catch { e ->
        Timber.e("getCoinChart error ${e.message}")
        emit(Result.Error("Unable to fetch coin chart"))
    }.flowOn(ioDispatcher)

    private fun CoinChartApiModel.toCoinChart(): CoinChart {
        val prices = coinChartData.history
            .orEmpty()
            .mapNotNull { it.price?.toBigDecimal() }
            .reversed()

        val minPrice = prices.min()
        val maxPrice = prices.max()

        val periodPriceChangePercentage = BigDecimal(coinChartData.pricePercentageChange)

        return CoinChart(
            prices = prices,
            minPrice = Price(minPrice),
            maxPrice = Price(maxPrice),
            periodPriceChangePercentage = Percentage(periodPriceChangePercentage)
        )
    }
}

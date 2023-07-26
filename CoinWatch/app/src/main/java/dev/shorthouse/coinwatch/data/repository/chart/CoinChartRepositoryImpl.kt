package dev.shorthouse.coinwatch.data.repository.chart

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import java.math.RoundingMode
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

                if (response.isSuccessful && body != null && body.prices.isNotEmpty()) {
                    Result.Success(body.toCoinChart())
                } else {
                    Timber.e("getCoinChart unsuccessful retrofit response ${response.message()}")
                    Result.Error(message = "Unable to fetch coin chart")
                }
            } catch (e: Throwable) {
                Timber.e("getCoinChart error ${e.message}")
                Result.Error(message = "Unable to fetch coin chart")
            }
        )
    }.flowOn(ioDispatcher)

    private fun CoinChartApiModel.toCoinChart(): CoinChart {
        val prices = this.prices.map { it.last() }
        val currentPrice = prices.last()

        val minPrice = prices.min()
        val minPriceChangePercentage = currentPrice.minus(minPrice)
            .divide(minPrice, RoundingMode.HALF_EVEN)
            .multiply(BigDecimal("100"))

        val maxPrice = prices.max()
        val maxPriceChangePercentage = currentPrice.minus(maxPrice)
            .divide(maxPrice, RoundingMode.HALF_EVEN)
            .multiply(BigDecimal("100"))

        val oldestPastPrice = prices.first()
        val periodPriceChangePercentage = currentPrice.minus(oldestPastPrice)
            .divide(oldestPastPrice, RoundingMode.HALF_EVEN)
            .multiply(BigDecimal("100"))

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

package dev.shorthouse.coinwatch.data.repository.chart

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.common.toSanitisedBigDecimalOrNull
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
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
        val prices = coinChartData.pastPrices
            .orEmpty()
            .mapNotNull { pastPrice ->
                pastPrice?.amount.toSanitisedBigDecimalOrNull()
            }
            .reversed()

        val minPrice = if (prices.isNotEmpty()) {
            prices.minOrNull().toString()
        } else {
            null
        }

        val maxPrice = if (prices.isNotEmpty()) {
            prices.maxOrNull().toString()
        } else {
            null
        }

        return CoinChart(
            prices = prices.toPersistentList(),
            minPrice = Price(minPrice),
            maxPrice = Price(maxPrice),
            periodPriceChangePercentage = Percentage(coinChartData.pricePercentageChange)
        )
    }
}

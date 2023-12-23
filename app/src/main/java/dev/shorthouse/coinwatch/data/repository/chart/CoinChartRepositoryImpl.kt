package dev.shorthouse.coinwatch.data.repository.chart

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.data.mapper.CoinChartMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.CoinChart
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class CoinChartRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val coinChartMapper: CoinChartMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinChartRepository {
    override fun getCoinChart(
        coinId: String,
        chartPeriod: String,
        currency: Currency
    ): Flow<Result<CoinChart>> = flow {
        val response = coinNetworkDataSource.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod,
            currency = currency
        )

        val body = response.body()

        if (response.isSuccessful && body?.coinChartData != null) {
            val coinChart = coinChartMapper.mapApiModelToModel(body, currency = currency)
            emit(Result.Success(coinChart))
        } else {
            Timber.e("getCoinChart unsuccessful retrofit response ${response.message()}")
            emit(Result.Error("Unable to fetch coin chart"))
        }
    }.catch { e ->
        Timber.e("getCoinChart error ${e.message}")
        emit(Result.Error("Unable to fetch coin chart"))
    }.flowOn(ioDispatcher)
}

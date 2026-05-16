package dev.shorthouse.coinwatch.data.repository.chart

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.common.TimeProvider
import dev.shorthouse.coinwatch.data.mapper.CoinChartMapper
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.CoinChart
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class CoinChartRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val coinChartMapper: CoinChartMapper,
    private val timeProvider: TimeProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : CoinChartRepository {

    private data class CacheKey(
        val coinId: String,
        val chartPeriod: String,
        val currency: Currency,
    )

    private data class CachedChart(
        val coinChart: CoinChart,
        val cachedAtMillis: Long,
    )

    private val cache = ConcurrentHashMap<CacheKey, CachedChart>()

    override fun getCoinChart(
        coinId: String,
        chartPeriod: String,
        currency: Currency,
    ): Flow<Result<CoinChart>> = flow {
        val cacheKey = CacheKey(coinId = coinId, chartPeriod = chartPeriod, currency = currency)

        val cachedChart = getCachedChartOrNull(cacheKey)
        if (cachedChart != null) {
            emit(Result.Success(cachedChart))
            return@flow
        }

        val response = coinNetworkDataSource.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod,
            currency = currency
        )

        val body = response.body()

        if (response.isSuccessful && body?.coinChartData != null) {
            val coinChart = coinChartMapper.mapApiModelToModel(
                apiModel = body,
                currency = currency
            )

            putInCache(cacheKey, coinChart)

            emit(Result.Success(coinChart))
        } else {
            Timber.e("getCoinChart unsuccessful retrofit response ${response.message()}")
            emit(Result.Error("Unable to fetch coin chart"))
        }
    }.catch { e ->
        Timber.e("getCoinChart error ${e.message}")
        emit(Result.Error("Unable to fetch coin chart"))
    }.flowOn(ioDispatcher)

    private fun getCachedChartOrNull(cacheKey: CacheKey): CoinChart? {
        val cachedChart = cache[cacheKey] ?: return null

        val cacheAgeMillis = timeProvider.elapsedRealtimeMillis() - cachedChart.cachedAtMillis

        return if (cacheAgeMillis > CACHE_TTL_MILLIS) {
            cache.remove(cacheKey, cachedChart)
            null
        } else {
            cachedChart.coinChart
        }
    }

    private fun putInCache(cacheKey: CacheKey, coinChart: CoinChart) {
        val now = timeProvider.elapsedRealtimeMillis()

        removeExpiredCacheEntries(now)

        cache[cacheKey] = CachedChart(
            coinChart = coinChart,
            cachedAtMillis = now
        )

        if (cache.size > MAX_CACHE_SIZE) {
            removeOldestCacheEntry(excludedKey = cacheKey)
        }
    }

    private fun removeExpiredCacheEntries(now: Long) {
        cache.forEach { (key, cachedChart) ->
            val cacheAgeMillis = now - cachedChart.cachedAtMillis

            if (cacheAgeMillis > CACHE_TTL_MILLIS) {
                cache.remove(key, cachedChart)
            }
        }
    }

    private fun removeOldestCacheEntry(excludedKey: CacheKey) {
        val oldestEntry = cache
            .asSequence()
            .filter { (key, _) -> key != excludedKey }
            .minByOrNull { (_, cachedChart) -> cachedChart.cachedAtMillis }

        if (oldestEntry != null) {
            cache.remove(oldestEntry.key, oldestEntry.value)
        }
    }

    internal companion object {
        const val CACHE_TTL_MILLIS = 2 * 60 * 1000L
        const val MAX_CACHE_SIZE = 100
    }
}

package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinMapper
import dev.shorthouse.coinwatch.data.source.local.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class CoinRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val coinLocalDataSource: CoinLocalDataSource,
    private val coinMapper: CoinMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinRepository {
    override suspend fun getRemoteCoins(
        coinSort: CoinSort,
        currency: Currency
    ): Result<List<CachedCoin>> = withContext(ioDispatcher) {
        try {
            val response = coinNetworkDataSource.getCoins(
                coinSort = coinSort,
                currency = currency
            )

            val body = response.body()

            if (response.isSuccessful && body?.coinsData != null) {
                val coins = coinMapper.mapApiModelToModel(body, currency = currency)
                Result.Success(coins)
            } else {
                Timber.e("getRemoteCoins unsuccessful retrofit response ${response.message()}")
                Result.Error("Unable to fetch network coins list")
            }
        } catch (e: Exception) {
            Timber.e("getRemoteCoins error ${e.message}")
            Result.Error("Unable to fetch network coins list")
        }
    }

    override fun getCachedCoins(): Flow<Result<List<CachedCoin>>> {
        return coinLocalDataSource.getCoins()
            .map { Result.Success(it) }
            .catch { e ->
                Timber.e("getCachedCoins error ${e.message}")
                Result.Error<List<CachedCoin>>("Unable to fetch cached coins")
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateCachedCoins(coins: List<CachedCoin>) {
        withContext(ioDispatcher) {
            coinLocalDataSource.updateCoins(coins)
        }
    }
}

package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinMapper
import dev.shorthouse.coinwatch.data.source.local.datastore.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.local.database.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.database.model.Coin
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.di.IoDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val coinLocalDataSource: CoinLocalDataSource,
    private val coinMapper: CoinMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : CoinRepository {
    override suspend fun getRemoteCoins(
        coinSort: CoinSort,
        currency: Currency,
    ): Result<List<Coin>> = withContext(ioDispatcher) {
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e("getRemoteCoins error ${e.message}")
            Result.Error("Unable to fetch network coins list")
        }
    }

    override fun getCachedCoins(): Flow<Result<List<Coin>>> {
        return coinLocalDataSource.getCoins()
            .map<List<Coin>, Result<List<Coin>>> { Result.Success(it) }
            .catch { e ->
                Timber.e("getCachedCoins error ${e.message}")
                emit(Result.Error("Unable to fetch cached coins"))
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateCachedCoins(coins: List<Coin>) {
        withContext(ioDispatcher) {
            coinLocalDataSource.updateCoins(coins)
        }
    }
}

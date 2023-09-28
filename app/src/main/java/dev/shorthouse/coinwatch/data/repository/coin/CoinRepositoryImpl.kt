package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.Coin
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class CoinRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val coinMapper: CoinMapper
) : CoinRepository {
    override fun getCoins(currencyUUID: String): Flow<Result<List<Coin>>> = flow {
        val response = coinNetworkDataSource.getCoins(currencyUUID = currencyUUID)
        val body = response.body()

        if (response.isSuccessful && body != null && body.coinsData != null) {
            emit(Result.Success(coinMapper.mapApiModelToModel(body)))
        } else {
            Timber.e("getCoins unsuccessful retrofit response ${response.message()}")
            emit(Result.Error("Unable to fetch coins list"))
        }
    }.catch { e ->
        Timber.e("getCoins error ${e.message}")
        emit(Result.Error("Unable to fetch coins list"))
    }.flowOn(ioDispatcher)
}

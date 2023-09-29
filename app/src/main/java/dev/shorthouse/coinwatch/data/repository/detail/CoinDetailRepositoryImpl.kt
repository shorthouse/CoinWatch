package dev.shorthouse.coinwatch.data.repository.detail

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinDetailMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSourceImpl
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.CoinDetail
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class CoinDetailRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSourceImpl,
    private val coinDetailMapper: CoinDetailMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinDetailRepository {
    override fun getCoinDetail(coinId: String): Flow<Result<CoinDetail>> = flow {
        val response = coinNetworkDataSource.getCoinDetail(coinId = coinId)
        val body = response.body()

        if (response.isSuccessful && body?.coinDetailDataHolder?.coinDetailData != null) {
            val coinDetail = coinDetailMapper.mapApiModelToModel(body)
            emit(Result.Success(coinDetail))
        } else {
            Timber.e("getCoinDetail unsuccessful retrofit response ${response.message()}")
            emit(Result.Error("Unable to fetch coin details"))
        }
    }.catch { e ->
        Timber.e("getCoinDetail exception ${e.message}")
        emit(Result.Error("Unable to fetch coin details"))
    }.flowOn(ioDispatcher)
}

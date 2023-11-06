package dev.shorthouse.coinwatch.data.repository.details

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.Currency
import dev.shorthouse.coinwatch.data.mapper.CoinDetailsMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.CoinDetails
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class CoinDetailsRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val coinDetailsMapper: CoinDetailsMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinDetailsRepository {
    override fun getCoinDetails(coinId: String, currency: Currency): Flow<Result<CoinDetails>> =
        flow {
            val response = coinNetworkDataSource.getCoinDetails(
                coinId = coinId,
                currency = currency
            )

            val body = response.body()

            if (response.isSuccessful && body?.coinDetailsDataHolder?.coinDetailsData != null) {
                val coinDetails = coinDetailsMapper.mapApiModelToModel(body, currency = currency)
                emit(Result.Success(coinDetails))
            } else {
                Timber.e("getCoinDetails unsuccessful retrofit response ${response.message()}")
                emit(Result.Error("Unable to fetch coin details"))
            }
        }.catch { e ->
            Timber.e("getCoinDetails exception ${e.message}")
            emit(Result.Error("Unable to fetch coin details"))
        }.flowOn(ioDispatcher)
}

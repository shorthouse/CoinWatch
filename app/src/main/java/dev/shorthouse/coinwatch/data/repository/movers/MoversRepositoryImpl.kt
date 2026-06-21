package dev.shorthouse.coinwatch.data.repository.movers

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.MoversMapper
import dev.shorthouse.coinwatch.data.source.local.datastore.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.model.Movers
import timber.log.Timber
import javax.inject.Inject

class MoversRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val moversMapper: MoversMapper,
) : MoversRepository {
    override suspend fun getMovers(currency: Currency): Result<Movers> {
        return try {
            val gainersResponse = coinNetworkDataSource.getMovers(
                coinSort = CoinSort.Gainers,
                currency = currency
            )
            val losersResponse = coinNetworkDataSource.getMovers(
                coinSort = CoinSort.Losers,
                currency = currency
            )

            val gainersBody = gainersResponse.body()
            val losersBody = losersResponse.body()

            val movers = if (gainersBody != null && losersBody != null) {
                moversMapper.mapApiModelToModel(
                    gainersApiModel = gainersBody,
                    losersApiModel = losersBody,
                    currency = currency
                )
            } else {
                null
            }

            if (gainersResponse.isSuccessful && losersResponse.isSuccessful && movers != null) {
                Result.Success(movers)
            } else {
                Timber.e("getMovers unsuccessful retrofit response ${gainersResponse.message()}")
                Result.Error(ERROR_MESSAGE)
            }
        } catch (e: Exception) {
            Timber.e("getMovers error ${e.message}")
            Result.Error(ERROR_MESSAGE)
        }
    }

    private companion object {
        const val ERROR_MESSAGE = "Unable to fetch movers"
    }
}

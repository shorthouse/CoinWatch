package dev.shorthouse.coinwatch.data.repository.trendingCoins

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.TrendingCoinMapper
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.model.TrendingCoin
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import javax.inject.Inject

class TrendingCoinsRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val trendingCoinMapper: TrendingCoinMapper,
) : TrendingCoinsRepository {
    override suspend fun getTrendingCoins(currency: Currency): Result<List<TrendingCoin>> {
        return try {
            val response = coinNetworkDataSource.getTrendingCoins(currency = currency)
            val body = response.body()
            val trendingCoins = body?.let {
                trendingCoinMapper.mapApiModelToModel(it, currency = currency)
            }

            if (response.isSuccessful && !trendingCoins.isNullOrEmpty()) {
                Result.Success(trendingCoins)
            } else {
                Timber.e(
                    "getTrendingCoins unsuccessful retrofit response ${response.message()}"
                )
                Result.Error(ERROR_MESSAGE)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e("getTrendingCoins error ${e.message}")
            Result.Error(ERROR_MESSAGE)
        }
    }

    private companion object {
        const val ERROR_MESSAGE = "Unable to fetch trending coins"
    }
}

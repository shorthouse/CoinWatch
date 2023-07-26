package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinApiModel
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class CoinRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinRepository {
    override fun getCoins(): Flow<Result<List<Coin>>> = flow {
        emit(
            try {
                val response = coinNetworkDataSource.getCoins()
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    Result.Success(body.map { it.toCoin() })
                } else {
                    Timber.e("getCoins unsuccessful retrofit response ${response.message()}")
                    Result.Error(message = "Unable to fetch coins list")
                }
            } catch (e: Throwable) {
                Timber.e("getCoins error ${e.message}")
                Result.Error(message = "Unable to fetch coins list")
            }
        )
    }.flowOn(ioDispatcher)

    private fun CoinApiModel.toCoin(): Coin {
        return Coin(
            id = id,
            name = name,
            symbol = symbol.uppercase(),
            image = image,
            currentPrice = Price(currentPrice),
            marketCapRank = marketCapRank,
            priceChangePercentage24h = Percentage(priceChangePercentage24h),
            prices24h = sparkline7d.prices.takeLast(1.days.inWholeHours.toInt())
        )
    }
}

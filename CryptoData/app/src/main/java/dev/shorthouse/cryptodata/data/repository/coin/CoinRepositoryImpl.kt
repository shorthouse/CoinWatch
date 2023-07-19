package dev.shorthouse.cryptodata.data.repository.coin

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.cryptodata.data.source.remote.model.CoinApiModel
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.Percentage
import dev.shorthouse.cryptodata.model.Price
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

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
                    Result.Error(message = response.message())
                }
            } catch (e: Throwable) {
                Timber.e("getCoins error $e")
                Result.Error(message = e.message)
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

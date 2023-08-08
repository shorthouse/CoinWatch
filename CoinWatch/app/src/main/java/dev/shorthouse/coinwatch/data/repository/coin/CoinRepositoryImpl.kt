package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class CoinRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinRepository {
    override fun getCoins(): Flow<Result<List<Coin>>> = flow {
        val response = coinNetworkDataSource.getCoins()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            emit(Result.Success(body.toCoins()))
        } else {
            Timber.e(message = "getCoins unsuccessful retrofit response ${response.message()}")
            emit(Result.Error(message = "Unable to fetch coins list"))
        }
    }.catch { e ->
        Timber.e(message = "getCoins error ${e.message}")
        emit(Result.Error(message = "Unable to fetch coins list"))
    }.flowOn(ioDispatcher)

    private fun CoinsApiModel.toCoins(): List<Coin> {
        return coinsData.coins.map {
            Coin(
                id = it.id,
                name = it.name,
                symbol = it.symbol.uppercase(),
                image = it.iconUrl,
                currentPrice = Price(BigDecimal(it.currentPrice)),
                priceChangePercentage24h = Percentage(BigDecimal(it.priceChangePercentage24h)),
                prices24h = it.sparkline24h
            )
        }
    }
}

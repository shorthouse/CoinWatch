package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
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
    override fun getCoins(currencyUUID: String): Flow<Result<List<Coin>>> = flow {
        val response = coinNetworkDataSource.getCoins(currencyUUID = currencyUUID)
        val body = response.body()

        if (response.isSuccessful && body != null) {
            emit(Result.Success(body.toCoins()))
        } else {
            Timber.e("getCoins unsuccessful retrofit response ${response.message()}")
            emit(Result.Error("Unable to fetch coins list"))
        }
    }.catch { e ->
        Timber.e("getCoins error ${e.message}")
        emit(Result.Error("Unable to fetch coins list"))
    }.flowOn(ioDispatcher)

    private fun CoinsApiModel.toCoins(): List<Coin> {
        return coinsData.coins
            .orEmpty()
            .filterNotNull()
            .filter { it.id != null }
            .map { coinApiModel ->
                Coin(
                    id = coinApiModel.id!!,
                    name = coinApiModel.name.orEmpty(),
                    symbol = coinApiModel.symbol.orEmpty(),
                    imageUrl = coinApiModel.iconUrl.orEmpty(),
                    currentPrice = Price(coinApiModel.currentPrice),
                    priceChangePercentage24h = Percentage(coinApiModel.priceChangePercentage24h),
                    prices24h = coinApiModel.sparkline24h.orEmpty()
                )
            }
    }
}

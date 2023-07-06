package dev.shorthouse.cryptodata.data.repository.coin

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.cryptodata.data.source.remote.model.CoinApiModel
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.Price
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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
                    Result.Error(message = response.message())
                }
            } catch (e: Throwable) {
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
            priceChangePercentage24h = priceChangePercentage24h
        )
    }
}

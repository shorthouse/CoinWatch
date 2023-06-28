package dev.shorthouse.cryptodata.data.repository.coin

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.cryptodata.data.source.remote.model.CoinApiModel
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.Coin
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
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
        val response = coinNetworkDataSource.getCoins()

        if (response.isSuccessful) {
            val coins = response.body()!!.map {
                it.toCoin()
            }

            emit(Result.Success(coins))
        } else {
            emit(Result.Error())
        }
    }.flowOn(ioDispatcher)

    private fun CoinApiModel.toCoin(): Coin {
        val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
            currency = Currency.getInstance("USD")
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }

        return Coin(
            id = id,
            name = name,
            symbol = symbol.uppercase(),
            image = image,
            currentPrice = currencyFormat.format(currentPrice),
            marketCapRank = marketCapRank,
            priceChangePercentage24h = priceChangePercentage24h
        )
    }
}

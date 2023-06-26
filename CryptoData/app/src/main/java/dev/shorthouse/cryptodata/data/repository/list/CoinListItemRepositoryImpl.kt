package dev.shorthouse.cryptodata.data.repository.list

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.cryptodata.data.source.remote.model.CoinListItemApiModel
import dev.shorthouse.cryptodata.di.IoDispatcher
import dev.shorthouse.cryptodata.model.CoinListItem
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CoinListItemRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinListItemRepository {
    override fun getCoins(): Flow<Result<List<CoinListItem>>> = flow {
        val response = coinNetworkDataSource.getCoinListItems()

        if (response.isSuccessful) {
            val cryptocurrencies = response.body()!!.map {
                it.toCoinListItem()
            }

            emit(Result.Success(cryptocurrencies))
        } else {
            emit(Result.Error())
        }
    }.flowOn(ioDispatcher)

    private fun CoinListItemApiModel.toCoinListItem(): CoinListItem {
        return CoinListItem(
            id = id,
            symbol = symbol.uppercase(),
            name = name,
            image = image,
            currentPrice = currentPrice,
            priceChangePercentage = priceChangePercentage
        )
    }
}

package dev.shorthouse.cryptodata.data.repository.favouriteCoin

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.local.CoinLocalDataSource
import dev.shorthouse.cryptodata.data.source.local.model.FavouriteCoin
import dev.shorthouse.cryptodata.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FavouriteCoinRepositoryImpl @Inject constructor(
    private val coinLocalDataSource: CoinLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FavouriteCoinRepository {
    override fun getFavouriteCoins(): Flow<Result<List<FavouriteCoin>>> = flow {
        try {
            val coins = coinLocalDataSource.getAllFavouriteCoins()
            emit(Result.Success(coins))
        } catch (e: Exception) {
            emit(Result.Error(message = e.message))
        }
    }.flowOn(ioDispatcher)

    override suspend fun insertFavouriteCoin(favouriteCoin: FavouriteCoin) {
        coinLocalDataSource.insert(favouriteCoin)
    }

    override suspend fun deleteFavouriteCoin(favouriteCoin: FavouriteCoin) {
        coinLocalDataSource.delete(favouriteCoin)
    }
}

package dev.shorthouse.coinwatch.data.repository.favouriteCoin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class FavouriteCoinRepositoryImpl @Inject constructor(
    private val coinLocalDataSource: CoinLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FavouriteCoinRepository {
    override fun getFavouriteCoins(): Flow<Result<List<FavouriteCoin>>> {
        return coinLocalDataSource.getFavouriteCoins()
            .map { Result.Success(it) }
            .catch { e ->
                Timber.e("getFavouriteCoins error ${e.message}")
                Result.Error<List<FavouriteCoin>>(message = "Unable to fetch favourite coins")
            }
            .flowOn(ioDispatcher)
    }

    override fun isCoinFavourite(coinId: String): Flow<Result<Boolean>> {
        return coinLocalDataSource.isCoinFavourite(coinId)
            .map { Result.Success(it) }
            .catch { e ->
                Timber.e("isCoinFavourite error ${e.message}")
                Result.Error<Boolean>(message = "Unable to fetch coin favourite status")
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun insertFavouriteCoin(favouriteCoin: FavouriteCoin) {
        coinLocalDataSource.insert(favouriteCoin)
    }

    override suspend fun deleteFavouriteCoin(favouriteCoin: FavouriteCoin) {
        coinLocalDataSource.delete(favouriteCoin)
    }
}
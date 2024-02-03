package dev.shorthouse.coinwatch.data.repository.favouriteCoinId

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import dev.shorthouse.coinwatch.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class FavouriteCoinIdRepositoryImpl @Inject constructor(
    private val coinLocalDataSource: CoinLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FavouriteCoinIdRepository {
    override fun getFavouriteCoinIds(): Flow<Result<List<FavouriteCoinId>>> {
        return coinLocalDataSource.getFavouriteCoinIds()
            .map { Result.Success(it) }
            .catch { e ->
                Timber.e("getFavouriteCoinIds error ${e.message}")
                Result.Error<List<FavouriteCoinId>>("Unable to fetch favourite coin ids")
            }
            .flowOn(ioDispatcher)
    }

    override fun isCoinFavourite(favouriteCoinId: FavouriteCoinId): Flow<Result<Boolean>> {
        return coinLocalDataSource.isCoinFavourite(favouriteCoinId = favouriteCoinId)
            .map { Result.Success(it) }
            .catch { e ->
                Timber.e("isCoinFavourite error ${e.message}")
                Result.Error<Boolean>("Unable to fetch if coin is favourite")
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun toggleIsCoinFavourite(favouriteCoinId: FavouriteCoinId) {
        withContext(ioDispatcher) {
            coinLocalDataSource.toggleIsCoinFavourite(favouriteCoinId)
        }
    }
}

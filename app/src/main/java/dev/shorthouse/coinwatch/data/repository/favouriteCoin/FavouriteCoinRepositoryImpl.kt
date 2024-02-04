package dev.shorthouse.coinwatch.data.repository.favouriteCoin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.FavouriteCoinMapper
import dev.shorthouse.coinwatch.data.source.local.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.model.Coin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class FavouriteCoinRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val coinLocalDataSource: CoinLocalDataSource,
    private val favouriteCoinMapper: FavouriteCoinMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FavouriteCoinRepository {
    override suspend fun getRemoteFavouriteCoins(
        coinIds: List<String>,
        coinSort: CoinSort,
        currency: Currency
    ): Result<List<FavouriteCoin>> {
        if (coinIds.isEmpty()) {
            return Result.Success(emptyList())
        }

        return try {
            val response = coinNetworkDataSource.getFavouriteCoins(
                coinIds = coinIds,
                coinSort = coinSort,
                currency = currency
            )

            val body = response.body()

            if (response.isSuccessful && body?.favouriteCoinsData?.favouriteCoins != null) {
                val favouriteCoins = favouriteCoinMapper.mapApiModelToModel(
                    apiModel = body,
                    currency = currency
                )
                Result.Success(favouriteCoins)
            } else {
                Timber.e(
                    "getRemoteFavouriteCoins unsuccessful retrofit response ${response.message()}"
                )
                Result.Error("Unable to fetch network favourite coins list")
            }
        } catch (e: Exception) {
            Timber.e("getRemoteFavouriteCoins error ${e.message}")
            Result.Error("Unable to fetch network favourite coins list")
        }
    }

    override fun getCachedFavouriteCoins(): Flow<Result<List<FavouriteCoin>>> {
        return coinLocalDataSource.getFavouriteCoins()
            .map { Result.Success(it) }
            .catch { e ->
                Timber.e("getCachedFavouriteCoins error ${e.message}")
                Result.Error<List<Coin>>("Unable to fetch cached favourite coins")
            }.flowOn(ioDispatcher)
    }

    override suspend fun updateCachedFavouriteCoins(favouriteCoins: List<FavouriteCoin>) {
        withContext(ioDispatcher) {
            coinLocalDataSource.updateFavouriteCoins(favouriteCoins)
        }
    }
}

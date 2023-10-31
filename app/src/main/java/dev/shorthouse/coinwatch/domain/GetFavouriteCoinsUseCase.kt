package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.UserPreferencesRepository
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.model.Coin
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class GetFavouriteCoinsUseCase @Inject constructor(
    private val favouriteCoinRepository: FavouriteCoinRepository,
    private val coinRepository: CoinRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Result<List<Coin>>> {
        return getFavouriteCoins()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getFavouriteCoins(): Flow<Result<List<Coin>>> {
        return favouriteCoinRepository.getFavouriteCoins().flatMapLatest { favouriteCoinsResult ->
            when (favouriteCoinsResult) {
                is Result.Success -> {
                    val favouriteCoinIds = favouriteCoinsResult.data.map { it.id }
                    getCoins(favouriteCoinIds = favouriteCoinIds)
                }

                is Result.Error -> {
                    flow {
                        emit(Result.Error(favouriteCoinsResult.message))
                    }
                }
            }
        }
    }

    private fun getCoins(favouriteCoinIds: List<String>): Flow<Result<List<Coin>>> {
        return if (favouriteCoinIds.isEmpty()) {
            flow {
                emit(Result.Success(emptyList()))
            }
        } else {
            userPreferencesRepository.userPreferencesFlow.flatMapLatest { userPreferences ->
                coinRepository.getCoins(
                    coinIds = favouriteCoinIds,
                    coinSort = userPreferences.coinSort,
                    currency = userPreferences.currency
                )
            }
        }
    }
}

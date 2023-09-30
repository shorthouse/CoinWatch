package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.model.Coin
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetFavouriteCoinsUseCase @Inject constructor(
    private val favouriteCoinRepository: FavouriteCoinRepository,
    private val coinsRepository: CoinRepository
) {
    operator fun invoke(): Flow<Result<List<Coin>>> {
        return getFavouriteCoins()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getFavouriteCoins(): Flow<Result<List<Coin>>> {
        val favouriteCoinsFlow = favouriteCoinRepository.getFavouriteCoins()

        return favouriteCoinsFlow.flatMapLatest { favouriteCoinsResult ->
            when (favouriteCoinsResult) {
                is Result.Success -> {
                    val favouriteCoinIds = favouriteCoinsResult.data.map { it.id }

                    if (favouriteCoinIds.isNotEmpty()) {
                        coinsRepository.getCoins(coinIds = favouriteCoinIds)
                    } else {
                        flow {
                            emit(Result.Success(emptyList()))
                        }
                    }
                }

                is Result.Error -> {
                    flow {
                        emit(Result.Error(favouriteCoinsResult.message))
                    }
                }
            }
        }
    }
}

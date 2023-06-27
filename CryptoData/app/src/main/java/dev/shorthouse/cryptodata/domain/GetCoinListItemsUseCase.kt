package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.repository.list.CoinListItemRepository
import dev.shorthouse.cryptodata.model.CoinListItem
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCoinListItemsUseCase @Inject constructor(
    private val coinListItemRepository: CoinListItemRepository
) {
    operator fun invoke(): Flow<Result<List<CoinListItem>>> {
        return getCoins()
    }

    private fun getCoins(): Flow<Result<List<CoinListItem>>> {
        return coinListItemRepository.getCoins()
    }
}

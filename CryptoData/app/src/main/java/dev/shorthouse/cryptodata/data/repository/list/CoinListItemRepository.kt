package dev.shorthouse.cryptodata.data.repository.list

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.model.CoinListItem
import kotlinx.coroutines.flow.Flow

interface CoinListItemRepository {
    fun getCoins(): Flow<Result<List<CoinListItem>>>
}

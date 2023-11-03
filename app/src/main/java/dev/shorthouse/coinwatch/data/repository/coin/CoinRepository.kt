package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.CoinSort
import dev.shorthouse.coinwatch.data.datastore.Currency
import dev.shorthouse.coinwatch.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(
        coinIds: List<String> = emptyList(),
        coinSort: CoinSort,
        currency: Currency
    ): Flow<Result<List<Coin>>>
}

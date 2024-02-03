package dev.shorthouse.coinwatch.data.repository.coin

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(
        coinSort: CoinSort,
        currency: Currency
    ): Flow<Result<List<Coin>>>
}

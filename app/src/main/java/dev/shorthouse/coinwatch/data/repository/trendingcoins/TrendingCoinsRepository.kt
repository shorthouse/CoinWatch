package dev.shorthouse.coinwatch.data.repository.trendingcoins

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.TrendingCoin

interface TrendingCoinsRepository {
    suspend fun getTrendingCoins(currency: Currency): Result<List<TrendingCoin>>
}

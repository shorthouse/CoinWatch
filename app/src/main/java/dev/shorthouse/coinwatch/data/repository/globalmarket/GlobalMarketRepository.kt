package dev.shorthouse.coinwatch.data.repository.globalmarket

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.GlobalMarket

interface GlobalMarketRepository {
    suspend fun getGlobalMarket(currency: Currency): Result<GlobalMarket>
}

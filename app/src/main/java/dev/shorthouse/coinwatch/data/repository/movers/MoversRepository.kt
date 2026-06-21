package dev.shorthouse.coinwatch.data.repository.movers

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.Movers

interface MoversRepository {
    suspend fun getMovers(currency: Currency): Result<Movers>
}

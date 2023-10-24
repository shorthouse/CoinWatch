package dev.shorthouse.coinwatch.data.repository.details

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.model.CoinDetails
import kotlinx.coroutines.flow.Flow

interface CoinDetailsRepository {
    fun getCoinDetails(coinId: String): Flow<Result<CoinDetails>>
}

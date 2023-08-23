package dev.shorthouse.coinwatch.data.repository.detail

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.model.CoinDetail
import kotlinx.coroutines.flow.Flow

interface CoinDetailRepository {
    fun getCoinDetail(coinId: String): Flow<Result<CoinDetail>>
}

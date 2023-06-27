package dev.shorthouse.cryptodata.data.repository.detail

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.model.CoinDetail
import kotlinx.coroutines.flow.Flow

interface CoinDetailRepository {
    fun getCoinDetail(coinId: String, periodDays: String): Flow<Result<CoinDetail>>
}

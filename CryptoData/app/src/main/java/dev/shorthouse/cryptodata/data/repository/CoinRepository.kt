package dev.shorthouse.cryptodata.data.repository

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.model.CoinPastPrices
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun getCoins(): Flow<Result<List<Coin>>>

    fun getCoinDetail(coinId: String): Flow<Result<CoinDetail>>

    fun getCoinPastPrices(coinId: String, periodDays: String): Flow<Result<CoinPastPrices>>
}

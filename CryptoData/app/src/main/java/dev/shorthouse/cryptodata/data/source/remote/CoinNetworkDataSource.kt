package dev.shorthouse.cryptodata.data.source.remote

import dev.shorthouse.cryptodata.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.cryptodata.data.source.remote.model.CoinListItemApiModel
import dev.shorthouse.cryptodata.data.source.remote.model.CoinPastPricesApiModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
import retrofit2.Response

class CoinNetworkDataSource @Inject constructor(
    private val coinApi: CoinApi
) {
    suspend fun getCoinListItems(): Response<List<CoinListItemApiModel>> {
        return coinApi.getCoinListItems()
    }

    suspend fun getCoinDetail(coinId: String): Response<CoinDetailApiModel> {
        return coinApi.getCoinDetail(coinId = coinId)
    }

    suspend fun getCoinPastPrices(
        coinId: String,
        periodDays: String
    ): Response<CoinPastPricesApiModel> {
        val test = 10.days
        return coinApi.getCoinPastPrices(coinId = coinId, periodDays = periodDays)
    }
}

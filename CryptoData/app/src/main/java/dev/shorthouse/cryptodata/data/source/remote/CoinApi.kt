package dev.shorthouse.cryptodata.data.source.remote

import dev.shorthouse.cryptodata.data.source.remote.dto.CoinDetailDto
import dev.shorthouse.cryptodata.data.source.remote.dto.CoinDto
import retrofit2.Response
import retrofit2.http.GET

interface CoinApi {
    @GET(
        "coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false&price_change_percentage=24h&locale=en&precision=2"
    )
    suspend fun getCryptocurrencies(): Response<List<CoinDto>>

    @GET(
        "coins/ethereum?localization=false&tickers=false&community_data=false&developer_data=false&sparkline=true"
    )
    suspend fun getCoinDetail(): Response<CoinDetailDto>
}

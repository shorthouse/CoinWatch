package dev.shorthouse.cryptodata.data.source.remote

import dev.shorthouse.cryptodata.model.Cryptocurrency
import retrofit2.http.GET

interface CryptocurrencyApi {
    @GET("/v1/coins")
    suspend fun getCryptocurrencies(): List<Cryptocurrency>
}

package dev.shorthouse.cryptodata.data

import dev.shorthouse.cryptodata.model.Cryptocurrency

interface CryptocurrencyRepository {
    suspend fun getCryptocurrencies(): List<Cryptocurrency>
}

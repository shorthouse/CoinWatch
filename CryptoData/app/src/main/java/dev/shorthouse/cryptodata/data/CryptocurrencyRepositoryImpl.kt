package dev.shorthouse.cryptodata.data

import dev.shorthouse.cryptodata.data.source.remote.CryptocurrencyApi
import dev.shorthouse.cryptodata.model.Cryptocurrency
import javax.inject.Inject

class CryptocurrencyRepositoryImpl @Inject constructor(
    private val api: CryptocurrencyApi,
) : CryptocurrencyRepository {
    override suspend fun getCryptocurrencies(): List<Cryptocurrency> {
        return api.getCryptocurrencies()
    }
}

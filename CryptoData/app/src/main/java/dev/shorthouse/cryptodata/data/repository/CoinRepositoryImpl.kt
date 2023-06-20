package dev.shorthouse.cryptodata.data.repository

import dev.shorthouse.cryptodata.common.Resource
import dev.shorthouse.cryptodata.data.source.remote.CryptocurrencyApi
import dev.shorthouse.cryptodata.data.source.remote.dto.toCoinDetail
import dev.shorthouse.cryptodata.data.source.remote.dto.toCryptocurrency
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.CoinDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CryptocurrencyApi,
) : CoinRepository {
    override suspend fun getCryptocurrencies(): Flow<Resource<List<Coin>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getCryptocurrencies()

            if (response.isSuccessful && response.body() != null) {
                val cryptocurrencies = response.body()!!.map {
                    it.toCryptocurrency()
                }

                emit(Resource.Success(cryptocurrencies))
            } else {
                val errorMessage = response.errorBody()?.string().orEmpty().ifEmpty {
                    "An unexpected error occurred"
                }

                emit(Resource.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override suspend fun getCoinDetail(): Flow<Resource<CoinDetail>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getCoinDetail()

            if (response.isSuccessful && response.body() != null) {
                val coinDetail = response.body()!!.toCoinDetail()

                emit(Resource.Success(coinDetail))
            } else {
                val errorMessage = response.errorBody()?.string().orEmpty().ifEmpty {
                    "An unexpected error occurred"
                }

                emit(Resource.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}

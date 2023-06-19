package dev.shorthouse.cryptodata.data.repository

import dev.shorthouse.cryptodata.common.Resource
import dev.shorthouse.cryptodata.data.source.remote.CryptocurrencyApi
import dev.shorthouse.cryptodata.data.source.remote.dto.toCryptocurrency
import dev.shorthouse.cryptodata.model.Cryptocurrency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class CryptocurrencyRepositoryImpl @Inject constructor(
    private val api: CryptocurrencyApi,
) : CryptocurrencyRepository {
    override suspend fun getCryptocurrencies(): Flow<Resource<List<Cryptocurrency>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getCryptocurrencies()

            if (response.isSuccessful) {
                val cryptocurrencies = response.body()?.map {
                    it.toCryptocurrency()
                } ?: emptyList()

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
}

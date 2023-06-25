package dev.shorthouse.cryptodata.data.repository

import dev.shorthouse.cryptodata.common.Result
import dev.shorthouse.cryptodata.data.source.remote.CoinApi
import dev.shorthouse.cryptodata.model.Coin
import dev.shorthouse.cryptodata.model.CoinDetail
import dev.shorthouse.cryptodata.model.CoinPastPrices
import dev.shorthouse.cryptodata.model.toCoin
import dev.shorthouse.cryptodata.model.toCoinDetail
import dev.shorthouse.cryptodata.model.toCoinPastPrices
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinApi
) : CoinRepository {
    override fun getCoins(): Flow<Result<List<Coin>>> = flow {
        emit(Result.Loading())

        try {
            val response = api.getCoins()

            if (response.isSuccessful && response.body() != null) {
                val cryptocurrencies = response.body()!!.map {
                    it.toCoin()
                }

                emit(Result.Success(cryptocurrencies))
            } else {
                val errorMessage = response.errorBody()?.string().orEmpty().ifEmpty {
                    "An unexpected error occurred"
                }

                emit(Result.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(Result.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getCoinDetail(coinId: String): Flow<Result<CoinDetail>> = flow {
        emit(Result.Loading())

        try {
            val response = api.getCoinDetail(coinId = coinId)

            if (response.isSuccessful && response.body() != null) {
                val coinDetail = response.body()!!.toCoinDetail()

                emit(Result.Success(coinDetail))
            } else {
                val errorMessage = response.errorBody()?.string().orEmpty().ifEmpty {
                    "An unexpected error occurred"
                }

                emit(Result.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(Result.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    override fun getCoinPastPrices(
        coinId: String,
        periodDays: String
    ): Flow<Result<CoinPastPrices>> = flow {
        emit(Result.Loading())

        try {
            val response = api.getCoinPrices(coinId = coinId, periodDays = periodDays)

            if (response.isSuccessful && response.body() != null) {
                val coinPrices = response.body()!!.toCoinPastPrices()

                emit(Result.Success(coinPrices))
            } else {
                val errorMessage = response.errorBody()?.string().orEmpty().ifEmpty {
                    "An unexpected error occurred"
                }

                emit(Result.Error(errorMessage))
            }
        } catch (e: IOException) {
            emit(Result.Error("Couldn't reach server. Check your internet connection"))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}

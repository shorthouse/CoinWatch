package dev.shorthouse.cryptodata.domain

import dev.shorthouse.cryptodata.common.Resource
import dev.shorthouse.cryptodata.data.CryptocurrencyRepository
import dev.shorthouse.cryptodata.model.Cryptocurrency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCryptocurrenciesUseCase @Inject constructor(
    private val repository: CryptocurrencyRepository,
) {
    operator fun invoke(): Flow<Resource<List<Cryptocurrency>>> = flow {
        try {
            emit(Resource.Loading())
            val cryptocurrency = repository.getCryptocurrencies()
            emit(Resource.Success(cryptocurrency))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}

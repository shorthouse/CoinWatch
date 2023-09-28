package dev.shorthouse.coinwatch.data.repository.searchResults

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.SearchCoin
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber

class CoinSearchResultsRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinSearchResultsRepository {
    override suspend fun getCoinSearchResults(searchQuery: String): Result<List<SearchCoin>> {
        val response = coinNetworkDataSource.getCoinSearchResults(searchQuery = searchQuery)
        val body = response.body()

        if (response.isSuccessful && body != null) {
            return Result.Success(body.toSearchCoins())
        } else {
            Timber.e("getCoinSearchResults unsuccessful retrofit response ${response.message()}")
            return Result.Error("Unable to fetch coin search results")
        }
    }

    private fun CoinSearchResultsApiModel.toSearchCoins(): List<SearchCoin> {
        return coinsSearchResultsData.coinSearchResults
            .orEmpty()
            .filterNotNull()
            .filter { it.id != null }
            .map { coinSearchResultsApiModel ->
                SearchCoin(
                    id = coinSearchResultsApiModel.id!!,
                    name = coinSearchResultsApiModel.name.orEmpty(),
                    symbol = coinSearchResultsApiModel.symbol.orEmpty(),
                    imageUrl = coinSearchResultsApiModel.iconUrl.orEmpty(),
                    currentPrice = Price(coinSearchResultsApiModel.currentPrice)
                )
            }
    }
}

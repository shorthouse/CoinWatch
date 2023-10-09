package dev.shorthouse.coinwatch.data.repository.searchResults

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinSearchResultsMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.di.IoDispatcher
import dev.shorthouse.coinwatch.model.SearchCoin
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class CoinSearchResultsRepositoryImpl @Inject constructor(
    private val coinNetworkDataSource: CoinNetworkDataSource,
    private val coinSearchResultsMapper: CoinSearchResultsMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CoinSearchResultsRepository {
    override suspend fun getCoinSearchResults(searchQuery: String): Result<List<SearchCoin>> {
        return try {
            withContext(ioDispatcher) {
                val response = coinNetworkDataSource.getCoinSearchResults(searchQuery = searchQuery)
                val body = response.body()

                if (response.isSuccessful && body?.coinsSearchResultsData != null) {
                    val coinSearchResults = coinSearchResultsMapper.mapApiModelToModel(body)
                    Result.Success(coinSearchResults)
                } else {
                    Timber.e(
                        "getCoinSearchResults unsuccessful retrofit response ${response.message()}"
                    )
                    Result.Error("Unable to fetch coin search results")
                }
            }
        } catch (e: Exception) {
            Timber.e("getCoinSearchResults error ${e.message}")
            Result.Error("Unable to fetch coin search results")
        }
    }
}

package dev.shorthouse.coinwatch.domain.search

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.searchResults.CoinSearchResultsRepository
import dev.shorthouse.coinwatch.model.SearchCoin
import javax.inject.Inject

class GetCoinSearchResultsUseCase @Inject constructor(
    private val coinSearchResultsRepository: CoinSearchResultsRepository
) {
    suspend operator fun invoke(searchQuery: String): Result<List<SearchCoin>> {
        return getCoinSearchResults(searchQuery = searchQuery)
    }

    private suspend fun getCoinSearchResults(searchQuery: String): Result<List<SearchCoin>> {
        return coinSearchResultsRepository.getCoinSearchResults(searchQuery = searchQuery)
    }
}

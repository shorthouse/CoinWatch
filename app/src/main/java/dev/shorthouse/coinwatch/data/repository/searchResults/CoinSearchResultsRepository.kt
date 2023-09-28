package dev.shorthouse.coinwatch.data.repository.searchResults

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.model.SearchCoin

interface CoinSearchResultsRepository {
    suspend fun getCoinSearchResults(searchQuery: String): Result<List<SearchCoin>>
}

package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.model.SearchCoin
import javax.inject.Inject

class CoinSearchResultsMapper @Inject constructor() {
    fun mapApiModelToModel(apiModel: CoinSearchResultsApiModel): List<SearchCoin> {
        val validSearchResultsCoins = apiModel.coinsSearchResultsData?.coinSearchResults
            .orEmpty()
            .filterNotNull()
            .filter { it.id != null }

        return validSearchResultsCoins.map { searchResultsCoin ->
            SearchCoin(
                id = searchResultsCoin.id!!,
                name = searchResultsCoin.name.orEmpty(),
                symbol = searchResultsCoin.symbol.orEmpty(),
                imageUrl = searchResultsCoin.imageUrl.orEmpty()
            )
        }
    }
}

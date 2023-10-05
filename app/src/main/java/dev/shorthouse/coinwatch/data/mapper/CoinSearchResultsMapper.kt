package dev.shorthouse.coinwatch.data.mapper

import dev.shorthouse.coinwatch.common.Mapper
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.model.SearchCoin
import javax.inject.Inject

class CoinSearchResultsMapper @Inject constructor() :
    Mapper<CoinSearchResultsApiModel, List<SearchCoin>> {
    override fun mapApiModelToModel(from: CoinSearchResultsApiModel): List<SearchCoin> {
        val validSearchResultsCoins = from.coinsSearchResultsData?.coinSearchResults
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

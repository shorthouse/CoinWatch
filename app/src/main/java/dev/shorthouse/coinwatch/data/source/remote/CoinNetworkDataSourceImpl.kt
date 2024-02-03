package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import javax.inject.Inject
import retrofit2.Response

class CoinNetworkDataSourceImpl @Inject constructor(
    private val coinApi: CoinApi
) : CoinNetworkDataSource {
    override suspend fun getCoins(
        coinIds: List<String>,
        coinSort: CoinSort,
        currency: Currency
    ): Response<CoinsApiModel> {
        return coinApi.getCoins(
            coinIds = coinIds,
            orderBy = coinSort.toOrderByString(),
            currencyUUID = currency.toCurrencyUUID()
        )
    }

    override suspend fun getFavouriteCoins(
        coinIds: List<String>,
        coinSort: CoinSort,
        currency: Currency
    ): Response<FavouriteCoinsApiModel> {
        return coinApi.getFavouriteCoins(
            coinIds = coinIds,
            orderBy = coinSort.toOrderByString(),
            currencyUUID = currency.toCurrencyUUID()
        )
    }

    override suspend fun getCoinDetails(
        coinId: String,
        currency: Currency
    ): Response<CoinDetailsApiModel> {
        return coinApi.getCoinDetails(
            coinId = coinId,
            currencyUUID = currency.toCurrencyUUID()
        )
    }

    override suspend fun getCoinChart(
        coinId: String,
        chartPeriod: String,
        currency: Currency
    ): Response<CoinChartApiModel> {
        return coinApi.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod,
            currencyUUID = currency.toCurrencyUUID()
        )
    }

    override suspend fun getCoinSearchResults(
        searchQuery: String
    ): Response<CoinSearchResultsApiModel> {
        return coinApi.getCoinSearchResults(searchQuery = searchQuery)
    }

    override suspend fun getMarketStats(): Response<MarketStatsApiModel> {
        return coinApi.getMarketStats()
    }
}

private fun CoinSort.toOrderByString(): String {
    return when (this) {
        CoinSort.MarketCap -> "marketCap"
        CoinSort.Price -> "price"
        CoinSort.PriceChange24h -> "change"
        CoinSort.Volume24h -> "24hVolume"
    }
}

private fun Currency.toCurrencyUUID(): String {
    return when (this) {
        Currency.USD -> "yhjMzLPhuIDl"
        Currency.GBP -> "Hokyui45Z38f"
        Currency.EUR -> "5k-_VTxqtCEI"
    }
}

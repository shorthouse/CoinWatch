package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import retrofit2.Response
import javax.inject.Inject

class CoinNetworkDataSourceImpl @Inject constructor(
    private val coinApi: CoinApi
) : CoinNetworkDataSource {
    override suspend fun getCoins(
        coinSort: CoinSort,
        currency: Currency
    ): Response<CoinsApiModel> {
        return coinApi.getCoins(
            orderBy = coinSort.getOrderBy(),
            orderDirection = coinSort.getOrderDirection(),
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
            orderBy = coinSort.getOrderBy(),
            orderDirection = coinSort.getOrderDirection(),
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

private fun Currency.toCurrencyUUID(): String {
    return when (this) {
        Currency.USD -> "yhjMzLPhuIDl"
        Currency.GBP -> "Hokyui45Z38f"
        Currency.EUR -> "5k-_VTxqtCEI"
    }
}

private fun CoinSort.getOrderBy(): String {
    return when (this) {
        CoinSort.MarketCap -> "marketCap"
        CoinSort.Popular -> "24hVolume"
        CoinSort.Gainers -> "change"
        CoinSort.Losers -> "change"
        CoinSort.Newest -> "listedAt"
    }
}

private fun CoinSort.getOrderDirection(): String {
    return when (this) {
        CoinSort.MarketCap -> "desc"
        CoinSort.Popular -> "desc"
        CoinSort.Gainers -> "desc"
        CoinSort.Losers -> "asc"
        CoinSort.Newest -> "desc"
    }
}

package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartData
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailsDataHolder
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsData
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsData
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsData
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsData
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsDataHolder
import dev.shorthouse.coinwatch.data.source.remote.model.PastPrice
import dev.shorthouse.coinwatch.fixture.Bitcoin
import dev.shorthouse.coinwatch.fixture.Ethereum
import retrofit2.Response
import javax.inject.Inject

class FakeCoinNetworkDataSource @Inject constructor() : CoinNetworkDataSource {

    var coinsResponse: Response<CoinsApiModel> = Response.success(defaultCoins())

    var favouriteCoinsResponse: Response<FavouriteCoinsApiModel> =
        Response.success(FavouriteCoinsApiModel(FavouriteCoinsData(emptyList())))

    var coinDetailsResponse: (coinId: String) -> Response<CoinDetailsApiModel> = { coinId ->
        Response.success(defaultCoinDetails(coinId))
    }

    var coinChartResponse: (
        coinId: String,
        chartPeriod: String,
    ) -> Response<CoinChartApiModel> = { _, _ ->
        Response.success(defaultCoinChart())
    }

    var coinSearchResultsResponse: (searchQuery: String) -> Response<CoinSearchResultsApiModel> =
        { _ ->
            Response.success(
                CoinSearchResultsApiModel(CoinSearchResultsData(emptyList()))
            )
        }

    var marketStatsResponse: Response<MarketStatsApiModel> = Response.success(defaultMarketStats())

    override suspend fun getCoins(
        coinSort: CoinSort,
        currency: Currency,
    ): Response<CoinsApiModel> = coinsResponse

    override suspend fun getFavouriteCoins(
        coinIds: List<String>,
        coinSort: CoinSort,
        currency: Currency,
    ): Response<FavouriteCoinsApiModel> = favouriteCoinsResponse

    override suspend fun getCoinDetails(
        coinId: String,
        currency: Currency,
    ): Response<CoinDetailsApiModel> = coinDetailsResponse(coinId)

    override suspend fun getCoinChart(
        coinId: String,
        chartPeriod: String,
        currency: Currency,
    ): Response<CoinChartApiModel> = coinChartResponse(coinId, chartPeriod)

    override suspend fun getCoinSearchResults(
        searchQuery: String,
    ): Response<CoinSearchResultsApiModel> = coinSearchResultsResponse(searchQuery)

    override suspend fun getMarketStats(): Response<MarketStatsApiModel> = marketStatsResponse

    private companion object {
        fun defaultCoins() = CoinsApiModel(
            coinsData = CoinsData(
                coins = listOf(Bitcoin.coinApiModel(), Ethereum.coinApiModel())
            )
        )

        fun defaultCoinDetails(coinId: String) = CoinDetailsApiModel(
            coinDetailsDataHolder = CoinDetailsDataHolder(
                coinDetailsData = if (coinId == Bitcoin.ID) Bitcoin.coinDetailsData() else null
            )
        )

        fun defaultCoinChart() = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = Bitcoin.PRICE_CHANGE_PERCENTAGE_24H,
                pastPrices = listOf(
                    PastPrice(amount = "29100", timestamp = 1700000000L),
                    PastPrice(amount = Bitcoin.RAW_PRICE, timestamp = 1700003600L)
                )
            )
        )

        fun defaultMarketStats() = MarketStatsApiModel(
            marketStatsDataHolder = MarketStatsDataHolder(
                marketStatsData = MarketStatsData(
                    marketCapChangePercentage24h = "1.2"
                )
            )
        )
    }
}

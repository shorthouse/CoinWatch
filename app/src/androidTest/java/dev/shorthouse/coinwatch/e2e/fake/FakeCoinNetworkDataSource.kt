package dev.shorthouse.coinwatch.e2e.fake

import dev.shorthouse.coinwatch.data.source.local.datastore.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
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
import dev.shorthouse.coinwatch.data.source.remote.model.FearGreedApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FearGreedDataPointApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsData
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsDataHolder
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalStatsData
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsData
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsDataHolder
import dev.shorthouse.coinwatch.data.source.remote.model.MoverCoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MoversApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MoversData
import dev.shorthouse.coinwatch.data.source.remote.model.PastPrice
import dev.shorthouse.coinwatch.data.source.remote.model.TrendingCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.TrendingCoinsData
import dev.shorthouse.coinwatch.e2e.fixture.Bitcoin
import dev.shorthouse.coinwatch.e2e.fixture.Ethereum
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

    var globalStatsResponse: Response<GlobalStatsApiModel> = Response.success(defaultGlobalStats())

    var globalMarketCoinStatsResponse: Response<GlobalMarketCoinStatsApiModel> =
        Response.success(defaultGlobalMarketCoinStats())

    var fearGreedResponse: Response<FearGreedApiModel> = Response.success(defaultFearGreed())

    var trendingCoinsResponse: Response<TrendingCoinsApiModel> =
        Response.success(defaultTrendingCoins())

    var moversResponse: (coinSort: CoinSort) -> Response<MoversApiModel> = { coinSort ->
        Response.success(defaultMovers(coinSort))
    }

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

    override suspend fun getGlobalStats(currency: Currency): Response<GlobalStatsApiModel> =
        globalStatsResponse

    override suspend fun getGlobalMarketCoinStats(
        currency: Currency,
    ): Response<GlobalMarketCoinStatsApiModel> = globalMarketCoinStatsResponse

    override suspend fun getFearGreed(): Response<FearGreedApiModel> = fearGreedResponse

    override suspend fun getTrendingCoins(
        currency: Currency,
    ): Response<TrendingCoinsApiModel> = trendingCoinsResponse

    override suspend fun getMovers(
        coinSort: CoinSort,
        currency: Currency,
    ): Response<MoversApiModel> = moversResponse(coinSort)

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

        fun defaultGlobalStats() = GlobalStatsApiModel(
            data = GlobalStatsData(
                totalMarketCap = "2410000000000",
                total24hVolume = "98200000000",
                btcDominance = 54.2
            )
        )

        fun defaultGlobalMarketCoinStats() = GlobalMarketCoinStatsApiModel(
            data = GlobalMarketCoinStatsDataHolder(
                stats = GlobalMarketCoinStatsData(
                    positiveChanges = 2841,
                    negativeChanges = 1893
                )
            )
        )

        fun defaultFearGreed() = FearGreedApiModel(
            data = listOf(
                FearGreedDataPointApiModel(timestamp = 1700000000L, value = 42.0),
                FearGreedDataPointApiModel(timestamp = 1700086400L, value = 44.0),
                FearGreedDataPointApiModel(timestamp = 1700172800L, value = 41.0)
            )
        )

        fun defaultTrendingCoins() = TrendingCoinsApiModel(
            trendingCoinsData = TrendingCoinsData(
                coins = listOf(
                    Bitcoin.trendingCoinApiModel(rank = 1),
                    Ethereum.trendingCoinApiModel(rank = 2)
                )
            )
        )

        fun defaultMovers(coinSort: CoinSort): MoversApiModel {
            val featured = if (coinSort == CoinSort.Gainers) {
                moverCoinApiModel(Bitcoin.ID, Bitcoin.SYMBOL, Bitcoin.NAME, Bitcoin.IMAGE_URL, "14.27")
            } else {
                moverCoinApiModel(Ethereum.ID, Ethereum.SYMBOL, Ethereum.NAME, Ethereum.IMAGE_URL, "-9.31")
            }
            return MoversApiModel(moversData = MoversData(coins = listOf(featured)))
        }

        private fun moverCoinApiModel(
            id: String,
            symbol: String,
            name: String,
            imageUrl: String,
            change: String,
        ) = MoverCoinApiModel(
            id = id,
            symbol = symbol,
            name = name,
            imageUrl = imageUrl,
            currentPrice = "29446.336548759988",
            priceChangePercentage24h = change,
            sparkline = listOf("29100", "29250", "29446")
        )
    }
}

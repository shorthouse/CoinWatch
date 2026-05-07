package dev.shorthouse.coinwatch.e2e.fixture

import dev.shorthouse.coinwatch.e2e.fake.FakeCoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartData
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResult
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsData
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsData
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsData
import dev.shorthouse.coinwatch.data.source.remote.model.PastPrice
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.math.BigDecimal

fun FakeCoinNetworkDataSource.respondWithCoins(vararg coins: CoinApiModel) {
    coinsResponse = Response.success(
        CoinsApiModel(
            coinsData = CoinsData(
                coins = coins.toList(),
            ),
        ),
    )
}

fun FakeCoinNetworkDataSource.respondWithFavouriteCoins(
    vararg favouriteCoins: FavouriteCoinApiModel,
) {
    favouriteCoinsResponse = Response.success(
        FavouriteCoinsApiModel(
            favouriteCoinsData = FavouriteCoinsData(
                favouriteCoins = favouriteCoins.toList(),
            ),
        ),
    )
}

fun FakeCoinNetworkDataSource.respondWithSearchResults(vararg searchResults: CoinSearchResult) {
    coinSearchResultsResponse = {
        Response.success(
            CoinSearchResultsApiModel(
                coinsSearchResultsData = CoinSearchResultsData(
                    coinSearchResults = searchResults.toList(),
                ),
            ),
        )
    }
}

fun FakeCoinNetworkDataSource.respondWithCoinChart(
    responseForChartPeriod: (chartPeriod: String) -> CoinChartApiModel,
) {
    coinChartResponse = { _, chartPeriod ->
        Response.success(responseForChartPeriod(chartPeriod))
    }
}

fun FakeCoinNetworkDataSource.failCoins() {
    coinsResponse = e2eErrorResponse()
}

fun FakeCoinNetworkDataSource.failFavouriteCoins() {
    favouriteCoinsResponse = e2eErrorResponse()
}

fun FakeCoinNetworkDataSource.failCoinDetails() {
    coinDetailsResponse = { e2eErrorResponse() }
}

fun FakeCoinNetworkDataSource.failCoinChart() {
    coinChartResponse = { _, _ -> e2eErrorResponse() }
}

fun FakeCoinNetworkDataSource.failSearchResults() {
    coinSearchResultsResponse = { e2eErrorResponse() }
}

fun FakeCoinNetworkDataSource.failMarketStats() {
    marketStatsResponse = e2eErrorResponse()
}

fun bitcoinSearchResult() = CoinSearchResult(
    id = Bitcoin.ID,
    symbol = Bitcoin.SYMBOL,
    name = Bitcoin.NAME,
    imageUrl = Bitcoin.IMAGE_URL,
)

fun ethereumSearchResult() = CoinSearchResult(
    id = Ethereum.ID,
    symbol = Ethereum.SYMBOL,
    name = Ethereum.NAME,
    imageUrl = Ethereum.IMAGE_URL,
)

fun coinChartApiModel(
    priceChangePercentage: String = Bitcoin.PRICE_CHANGE_PERCENTAGE_24H,
    pastPrices: List<PastPrice?> = listOf(
        PastPrice(amount = "29100", timestamp = 1700000000L),
        PastPrice(amount = Bitcoin.RAW_PRICE, timestamp = 1700003600L),
    ),
) = CoinChartApiModel(
    coinChartData = CoinChartData(
        priceChangePercentage = priceChangePercentage,
        pastPrices = pastPrices,
    ),
)

fun bitcoinFavouriteCoinApiModel() = FavouriteCoinApiModel(
    id = Bitcoin.ID,
    symbol = Bitcoin.SYMBOL,
    name = Bitcoin.NAME,
    imageUrl = Bitcoin.IMAGE_URL,
    currentPrice = Bitcoin.RAW_PRICE,
    priceChangePercentage24h = Bitcoin.PRICE_CHANGE_PERCENTAGE_24H,
    prices24h = listOf(
        BigDecimal("29390.15178296929"),
        BigDecimal("29428.222505493162"),
        BigDecimal(Bitcoin.RAW_PRICE),
    ),
)

fun ethereumFavouriteCoinApiModel() = FavouriteCoinApiModel(
    id = Ethereum.ID,
    symbol = Ethereum.SYMBOL,
    name = Ethereum.NAME,
    imageUrl = Ethereum.IMAGE_URL,
    currentPrice = Ethereum.RAW_PRICE,
    priceChangePercentage24h = Ethereum.PRICE_CHANGE_PERCENTAGE_24H,
    prices24h = listOf(
        BigDecimal("1872.5227299255032"),
        BigDecimal("1874.813847463032"),
        BigDecimal(Ethereum.RAW_PRICE),
    ),
)

private fun <T> e2eErrorResponse(): Response<T> {
    return Response.error(
        500,
        """{"message":"E2E fake error"}"""
            .toResponseBody("application/json".toMediaType()),
    )
}

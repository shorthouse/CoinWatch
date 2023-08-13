package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.data.source.remote.model.AllTimeHigh
import dev.shorthouse.coinwatch.data.source.remote.model.Coin
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartData
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetail
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinDetailData
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsData
import dev.shorthouse.coinwatch.data.source.remote.model.PastPrice
import dev.shorthouse.coinwatch.data.source.remote.model.Supply
import java.math.BigDecimal
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeCoinApi : CoinApi {
    override suspend fun getCoins(
        currencyUUID: String,
        timePeriod: String,
        orderBy: String,
        orderDirection: String,
        limit: String
    ): Response<CoinsApiModel> {
        return when (currencyUUID) {
            "USD" -> {
                Response.success(
                    CoinsApiModel(
                        coinsData = CoinsData(
                            coins = listOf(
                                Coin(
                                    id = "Qwsogvtv82FCd",
                                    symbol = "BTC",
                                    name = "Bitcoin",
                                    iconUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                                    currentPrice = "29490.954785191607",
                                    priceChangePercentage24h = "-0.96",
                                    sparkline24h = listOf(
                                        BigDecimal("29790.15810429195"),
                                        BigDecimal("29782.07714670252"),
                                        BigDecimal("29436.47984833588"),
                                        BigDecimal("29510.92753539824"),
                                        BigDecimal("29482.564008512305")
                                    )
                                )
                            )
                        )
                    )
                )
            }
            "nullValues" -> {
                Response.success(
                    CoinsApiModel(
                        coinsData = CoinsData(
                            coins = listOf(
                                Coin(
                                    id = "Qwsogvtv82FCd",
                                    symbol = null,
                                    name = null,
                                    iconUrl = null,
                                    currentPrice = null,
                                    priceChangePercentage24h = null,
                                    sparkline24h = null
                                )
                            )
                        )
                    )
                )
            }
            "nullCoins" -> {
                Response.success(
                    CoinsApiModel(
                        coinsData = CoinsData(
                            coins = null
                        )
                    )
                )
            }
            "nullBody" -> {
                Response.success(
                    null
                )
            }
            "nullIds" -> {
                Response.success(
                    CoinsApiModel(
                        coinsData = CoinsData(
                            coins = listOf(
                                Coin(
                                    id = null,
                                    symbol = "BTC",
                                    name = "Bitcoin",
                                    iconUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                                    currentPrice = "29490.954785191607",
                                    priceChangePercentage24h = "-0.96",
                                    sparkline24h = listOf(
                                        BigDecimal("29790.15810429195"),
                                        BigDecimal("29782.07714670252"),
                                        BigDecimal("29436.47984833588"),
                                        BigDecimal("29510.92753539824"),
                                        BigDecimal("29482.564008512305")
                                    )
                                ),
                                Coin(
                                    id = "razxDUgYGNAdQ",
                                    symbol = "ETH",
                                    name = "Ethereum",
                                    iconUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                                    currentPrice = "1845.7097788177032",
                                    priceChangePercentage24h = "0.42",
                                    sparkline24h = listOf(
                                        BigDecimal("1857.0635686120618"),
                                        BigDecimal("1852.7243420201132"),
                                        BigDecimal("1850.8054635160697"),
                                        BigDecimal("1848.197142458803"),
                                        BigDecimal("1847.2140162508354")
                                    )
                                )
                            )
                        )
                    )
                )
            }
            "exception" -> {
                throw IllegalArgumentException("Test exception")
            }
            else -> {
                Response.error(
                    404,
                    "Coins not found".toResponseBody(null)
                )
            }
        }
    }

    override suspend fun getCoinChart(
        coinId: String,
        currencyUUID: String,
        chartPeriod: String
    ): Response<CoinChartApiModel> {
        when (coinId) {
            "Qwsogvtv82FCd" -> {
                val coinChartData = CoinChartData(
                    pricePercentageChange = "-0.97",
                    pastPrices = listOf(
                        PastPrice(amount = "20000.20"),
                        PastPrice(amount = "30000.47"),
                        PastPrice(amount = null),
                        PastPrice(amount = "25000.89"),
                        PastPrice(amount = "27000.44")
                    )
                )

                return Response.success(
                    CoinChartApiModel(
                        coinChartData = coinChartData
                    )
                )
            }
            "nullValues" -> {
                val coinChartData = CoinChartData(
                    pricePercentageChange = null,
                    pastPrices = null
                )

                return Response.success(
                    CoinChartApiModel(
                        coinChartData = coinChartData
                    )
                )
            }
            "nullPrices" -> {
                val coinChartData = CoinChartData(
                    pricePercentageChange = null,
                    pastPrices = listOf(
                        PastPrice(amount = "30000.47"),
                        null,
                        PastPrice(amount = null),
                        PastPrice(amount = "25000.89"),
                        null
                    )
                )

                return Response.success(
                    CoinChartApiModel(
                        coinChartData = coinChartData
                    )
                )
            }
            "nullBody" -> {
                return Response.success(null)
            }
            else -> {
                return Response.error(
                    404,
                    "Chart not found".toResponseBody(null)
                )
            }
        }
    }

    override suspend fun getCoinDetail(
        coinId: String,
        currencyUUID: String
    ): Response<CoinDetailApiModel> {
        when (coinId) {
            "Qwsogvtv82FCd" -> {
                val coinDetail = CoinDetail(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    iconUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = "29488.266719247607",
                    marketCap = "573638201316",
                    marketCapRank = "1",
                    volume24h = "12314826429",
                    supply = Supply(
                        circulatingSupply = "19453100"
                    ),
                    allTimeHigh = AllTimeHigh(
                        price = "68763.41083248306",
                        timestamp = 1636502400
                    ),
                    listedAt = 1330214400
                )

                return Response.success(
                    CoinDetailApiModel(
                        coinDetailData = CoinDetailData(
                            coinDetail = coinDetail
                        )
                    )
                )
            }
            else -> {
                return Response.error(
                    404,
                    "Coin detail not found".toResponseBody(null)
                )
            }
        }
    }
}

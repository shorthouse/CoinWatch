package dev.shorthouse.coinwatch.data.repository.chart

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.common.TimeProvider
import dev.shorthouse.coinwatch.data.mapper.CoinChartMapper
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartData
import dev.shorthouse.coinwatch.data.source.remote.model.PastPrice
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.PriceEntry
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.math.BigDecimal

class CoinChartRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var coinChartRepository: CoinChartRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    private val fakeTimeProvider = FakeTimeProvider()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coinChartRepository = CoinChartRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinChartMapper = CoinChartMapper(),
            timeProvider = fakeTimeProvider,
            ioDispatcher = mainDispatcherRule.testDispatcher
        )
    }

    private class FakeTimeProvider(var nowMillis: Long = 0L) : TimeProvider {
        override fun elapsedRealtimeMillis(): Long = nowMillis
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When coin chart data is valid should return success`() = runTest {
        // Arrange
        val coinId = "Qwsogvtv82FCd"
        val chartPeriod = "1d"
        val currency = Currency.USD

        val expectedResult = Result.Success(
            CoinChart(
                currency = currency,
                priceHistory = persistentListOf(
                    PriceEntry(BigDecimal("27000.44"), 1700014400L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("25000.89"), 1700010800L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("30000.47"), 1700007200L, "15 Nov 2023"),
                    PriceEntry(BigDecimal("20000.20"), 1700003600L, "14 Nov 2023")
                ),
                periodPriceChangePercentage = Percentage("-0.97")
            )
        )

        coEvery {
            coinNetworkDataSource.getCoinChart(
                coinId = coinId,
                chartPeriod = chartPeriod,
                currency = currency
            )
        } returns Response.success(
            CoinChartApiModel(
                coinChartData = CoinChartData(
                    priceChangePercentage = "-0.97",
                    pastPrices = listOf(
                        PastPrice(amount = "20000.20", timestamp = 1700003600L),
                        PastPrice(amount = "30000.47", timestamp = 1700007200L),
                        PastPrice(amount = null, timestamp = 1700000000L),
                        PastPrice(amount = "25000.89", timestamp = 1700010800L),
                        PastPrice(amount = "27000.44", timestamp = 1700014400L)
                    )
                )
            )
        )

        // Act
        val result = coinChartRepository.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod,
            currency = currency
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When coin chart data has null values should populate these with default values and return success`() =
        runTest {
            // Arrange
            val coinId = "Qwsogvtv82FCd"
            val chartPeriod = "1d"
            val currency = Currency.USD

            val expectedPriceHistory = persistentListOf<PriceEntry>()
            val expectedPeriodPriceChangePercentageAmount = BigDecimal.ZERO

            coEvery {
                coinNetworkDataSource.getCoinChart(
                    coinId = coinId,
                    chartPeriod = chartPeriod,
                    currency = currency
                )
            } returns Response.success(
                CoinChartApiModel(
                    coinChartData = CoinChartData(
                        priceChangePercentage = null,
                        pastPrices = null
                    )
                )
            )

            // Act
            val result = coinChartRepository.getCoinChart(
                coinId = coinId,
                chartPeriod = chartPeriod,
                currency = currency
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)

            assertThat((result as Result.Success).data.currency).isEqualTo(currency)
            assertThat(result.data.priceHistory).isEqualTo(expectedPriceHistory)
            assertThat(result.data.periodPriceChangePercentage.amount).isEqualTo(
                expectedPeriodPriceChangePercentageAmount
            )
        }

    @Test
    fun `When coin chart has null prices should ignore these prices and return success`() =
        runTest {
            // Arrange
            val coinId = "Qwsogvtv82FCd"
            val chartPeriod = "1d"
            val currency = Currency.USD

            val expectedPriceHistory = persistentListOf(
                PriceEntry(BigDecimal("25000.89"), 1700010800L, "15 Nov 2023"),
                PriceEntry(BigDecimal("30000.47"), 1700003600L, "14 Nov 2023")
            )

            coEvery {
                coinNetworkDataSource.getCoinChart(
                    coinId = coinId,
                    chartPeriod = chartPeriod,
                    currency = currency
                )
            } returns Response.success(
                CoinChartApiModel(
                    coinChartData = CoinChartData(
                        priceChangePercentage = null,
                        pastPrices = listOf(
                            PastPrice(amount = "30000.47", timestamp = 1700003600L),
                            null,
                            PastPrice(amount = null, timestamp = 1700007200L),
                            PastPrice(amount = "25000.89", timestamp = 1700010800L),
                            null
                        )
                    )
                )
            )

            // Act
            val result = coinChartRepository.getCoinChart(
                coinId = coinId,
                chartPeriod = chartPeriod,
                currency = currency
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data.currency).isEqualTo(currency)
            assertThat(result.data.priceHistory)
                .isEqualTo(expectedPriceHistory)
        }

    @Test
    fun `When same args called multiple times within TTL, should call network only once and return cached chart`() =
        runTest {
            // Arrange
            val coinId = "Qwsogvtv82FCd"
            val chartPeriod = "1d"
            val currency = Currency.USD

            coEvery {
                coinNetworkDataSource.getCoinChart(coinId, chartPeriod, currency)
            } returns successResponse()

            // Act
            val firstResult = coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()
            val secondResult = coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()
            val thirdResult = coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()

            // Assert
            coVerify(exactly = 1) {
                coinNetworkDataSource.getCoinChart(coinId, chartPeriod, currency)
            }
            assertThat(firstResult).isInstanceOf(Result.Success::class.java)
            assertThat((secondResult as Result.Success).data)
                .isEqualTo((firstResult as Result.Success).data)
            assertThat((thirdResult as Result.Success).data).isEqualTo(firstResult.data)
        }

    @Test
    fun `When different coin ids requested, should call network for each and cache independently`() =
        runTest {
            // Arrange
            val chartPeriod = "1d"
            val currency = Currency.USD
            val firstCoinId = "Qwsogvtv82FCd"
            val secondCoinId = "razxDUgYGNAdQ"

            coEvery {
                coinNetworkDataSource.getCoinChart(firstCoinId, chartPeriod, currency)
            } returns successResponse(price = "27000.44")
            coEvery {
                coinNetworkDataSource.getCoinChart(secondCoinId, chartPeriod, currency)
            } returns successResponse(price = "1800.00")

            // Act
            coinChartRepository.getCoinChart(firstCoinId, chartPeriod, currency).first()
            coinChartRepository.getCoinChart(secondCoinId, chartPeriod, currency).first()
            coinChartRepository.getCoinChart(firstCoinId, chartPeriod, currency).first()
            coinChartRepository.getCoinChart(secondCoinId, chartPeriod, currency).first()

            // Assert
            coVerify(exactly = 1) {
                coinNetworkDataSource.getCoinChart(firstCoinId, chartPeriod, currency)
            }
            coVerify(exactly = 1) {
                coinNetworkDataSource.getCoinChart(secondCoinId, chartPeriod, currency)
            }
        }

    @Test
    fun `When different chart periods requested, should call network for each and cache independently`() =
        runTest {
            // Arrange
            val coinId = "Qwsogvtv82FCd"
            val currency = Currency.USD
            val dayPeriod = "1d"
            val weekPeriod = "7d"

            coEvery {
                coinNetworkDataSource.getCoinChart(coinId, dayPeriod, currency)
            } returns successResponse()
            coEvery {
                coinNetworkDataSource.getCoinChart(coinId, weekPeriod, currency)
            } returns successResponse()

            // Act
            coinChartRepository.getCoinChart(coinId, dayPeriod, currency).first()
            coinChartRepository.getCoinChart(coinId, weekPeriod, currency).first()
            coinChartRepository.getCoinChart(coinId, dayPeriod, currency).first()
            coinChartRepository.getCoinChart(coinId, weekPeriod, currency).first()

            // Assert
            coVerify(exactly = 1) {
                coinNetworkDataSource.getCoinChart(coinId, dayPeriod, currency)
            }
            coVerify(exactly = 1) {
                coinNetworkDataSource.getCoinChart(coinId, weekPeriod, currency)
            }
        }

    @Test
    fun `When different currencies requested, should call network for each and cache independently`() =
        runTest {
            // Arrange
            val coinId = "Qwsogvtv82FCd"
            val chartPeriod = "1d"

            coEvery {
                coinNetworkDataSource.getCoinChart(coinId, chartPeriod, Currency.USD)
            } returns successResponse()
            coEvery {
                coinNetworkDataSource.getCoinChart(coinId, chartPeriod, Currency.GBP)
            } returns successResponse()

            // Act
            coinChartRepository.getCoinChart(coinId, chartPeriod, Currency.USD).first()
            coinChartRepository.getCoinChart(coinId, chartPeriod, Currency.GBP).first()
            coinChartRepository.getCoinChart(coinId, chartPeriod, Currency.USD).first()
            coinChartRepository.getCoinChart(coinId, chartPeriod, Currency.GBP).first()

            // Assert
            coVerify(exactly = 1) {
                coinNetworkDataSource.getCoinChart(coinId, chartPeriod, Currency.USD)
            }
            coVerify(exactly = 1) {
                coinNetworkDataSource.getCoinChart(coinId, chartPeriod, Currency.GBP)
            }
        }

    @Test
    fun `When first call returns error, should not cache and should retry network on next call`() =
        runTest {
            // Arrange
            val coinId = "Qwsogvtv82FCd"
            val chartPeriod = "1d"
            val currency = Currency.USD

            coEvery {
                coinNetworkDataSource.getCoinChart(coinId, chartPeriod, currency)
            } returns Response.error(500, "Server error".toResponseBody(null)) andThen successResponse()

            // Act
            val firstResult = coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()
            val secondResult = coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()

            // Assert
            coVerify(exactly = 2) {
                coinNetworkDataSource.getCoinChart(coinId, chartPeriod, currency)
            }
            assertThat(firstResult).isInstanceOf(Result.Error::class.java)
            assertThat(secondResult).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun `When cached entry exceeds TTL, should evict and refetch from network`() = runTest {
        // Arrange
        val coinId = "Qwsogvtv82FCd"
        val chartPeriod = "1d"
        val currency = Currency.USD

        coEvery {
            coinNetworkDataSource.getCoinChart(coinId, chartPeriod, currency)
        } returns successResponse()

        // Act
        fakeTimeProvider.nowMillis = 0L
        coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()

        fakeTimeProvider.nowMillis = 60_000L
        coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()

        fakeTimeProvider.nowMillis = CoinChartRepositoryImpl.CACHE_TTL_MILLIS + 1L
        coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()

        // Assert
        coVerify(exactly = 2) {
            coinNetworkDataSource.getCoinChart(coinId, chartPeriod, currency)
        }
    }

    @Test
    fun `When TTL refetch occurs, new entry's TTL window starts from refetch time`() = runTest {
        // Arrange
        val coinId = "Qwsogvtv82FCd"
        val chartPeriod = "1d"
        val currency = Currency.USD

        coEvery {
            coinNetworkDataSource.getCoinChart(coinId, chartPeriod, currency)
        } returns successResponse()

        // Act
        fakeTimeProvider.nowMillis = 0L
        coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()

        fakeTimeProvider.nowMillis = CoinChartRepositoryImpl.CACHE_TTL_MILLIS + 1L
        coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()

        fakeTimeProvider.nowMillis = CoinChartRepositoryImpl.CACHE_TTL_MILLIS + 1L + 60_000L
        coinChartRepository.getCoinChart(coinId, chartPeriod, currency).first()

        // Assert
        coVerify(exactly = 2) {
            coinNetworkDataSource.getCoinChart(coinId, chartPeriod, currency)
        }
    }

    @Test
    fun `When cache reaches max size, should evict oldest entry to make room for new one`() = runTest {
        // Arrange
        val chartPeriod = "1d"
        val currency = Currency.USD

        coEvery {
            coinNetworkDataSource.getCoinChart(any(), chartPeriod, currency)
        } returns successResponse()

        // Act
        repeat(CoinChartRepositoryImpl.MAX_CACHE_SIZE) { index ->
            fakeTimeProvider.nowMillis = index.toLong()
            coinChartRepository.getCoinChart("coin-$index", chartPeriod, currency).first()
        }

        fakeTimeProvider.nowMillis = 10_000L
        coinChartRepository.getCoinChart("overflow-coin", chartPeriod, currency).first()

        coinChartRepository.getCoinChart("coin-0", chartPeriod, currency).first()

        // Assert
        coVerify(exactly = CoinChartRepositoryImpl.MAX_CACHE_SIZE + 2) {
            coinNetworkDataSource.getCoinChart(any(), chartPeriod, currency)
        }
    }

    private fun successResponse(
        price: String = "27000.44",
        timestamp: Long = 1700014400L
    ): Response<CoinChartApiModel> {
        return Response.success(
            CoinChartApiModel(
                coinChartData = CoinChartData(
                    priceChangePercentage = "-0.97",
                    pastPrices = listOf(PastPrice(amount = price, timestamp = timestamp))
                )
            )
        )
    }

    @Test
    fun `When coin chart returns error should return error`() = runTest {
        // Arrange
        val coinId = "Qwditjgs82FCd"
        val chartPeriod = "1d"
        val currency = Currency.USD

        val expectedResult = Result.Error<CoinChartApiModel>(
            message = "Unable to fetch coin chart"
        )

        coEvery {
            coinNetworkDataSource.getCoinChart(
                coinId = coinId,
                chartPeriod = chartPeriod,
                currency = currency
            )
        } returns Response.error(
            404,
            "Chart not found".toResponseBody(null)
        )

        // Act
        val result = coinChartRepository.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod,
            currency = currency
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }
}

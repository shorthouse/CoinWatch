package dev.shorthouse.coinwatch.data.repository.chart

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinChartMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartData
import dev.shorthouse.coinwatch.data.source.remote.model.PastPrice
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coinChartRepository = CoinChartRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinChartMapper = CoinChartMapper(),
            ioDispatcher = mainDispatcherRule.testDispatcher
        )
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
                prices = persistentListOf(
                    BigDecimal("27000.44"),
                    BigDecimal("25000.89"),
                    BigDecimal("30000.47"),
                    BigDecimal("20000.20")
                ),
                minPrice = Price("20000.20"),
                maxPrice = Price("30000.47"),
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
                        PastPrice(amount = "20000.20"),
                        PastPrice(amount = "30000.47"),
                        PastPrice(amount = null),
                        PastPrice(amount = "25000.89"),
                        PastPrice(amount = "27000.44")
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

            val expectedPrices = emptyList<BigDecimal>()
            val expectedMinPriceAmount = BigDecimal.ZERO
            val expectedMaxPriceAmount = BigDecimal.ZERO
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

            assertThat((result as Result.Success).data.prices).isEqualTo(expectedPrices)
            assertThat(result.data.minPrice.amount).isEqualTo(expectedMinPriceAmount)
            assertThat(result.data.maxPrice.amount).isEqualTo(expectedMaxPriceAmount)
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

            val expectedPrices = listOf(
                BigDecimal("25000.89"),
                BigDecimal("30000.47")
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
                            PastPrice(amount = "30000.47"),
                            null,
                            PastPrice(amount = null),
                            PastPrice(amount = "25000.89"),
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
            assertThat((result as Result.Success).data.prices).isEqualTo(expectedPrices)
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

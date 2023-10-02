package dev.shorthouse.coinwatch.data.repository.chart

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinChartMapper
import dev.shorthouse.coinwatch.data.source.remote.FakeCoinApi
import dev.shorthouse.coinwatch.data.source.remote.FakeCoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoinChartRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var coinChartRepository: CoinChartRepository

    @Before
    fun setup() {
        coinChartRepository = CoinChartRepositoryImpl(
            coinNetworkDataSource = FakeCoinNetworkDataSource(
                coinApi = FakeCoinApi()
            ),
            coinChartMapper = CoinChartMapper(),
            ioDispatcher = mainDispatcherRule.testDispatcher
        )
    }

    @Test
    fun `When coin chart data is valid should return success`() = runTest {
        // Arrange
        val coinId = "Qwsogvtv82FCd"
        val chartPeriod = "1d"

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

        // Act
        val result = coinChartRepository.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When coin chart data has null values should populate these with default values and return success`() = runTest {
        // Arrange
        val coinId = "nullValues"
        val chartPeriod = "1d"

        val expectedPrices = emptyList<BigDecimal>()
        val expectedMinPriceAmount = BigDecimal.ZERO
        val expectedMaxPriceAmount = BigDecimal.ZERO
        val expectedPeriodPriceChangePercentageAmount = BigDecimal.ZERO

        // Act
        val result = coinChartRepository.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod
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
    fun `When coin chart has null prices should ignore these prices and return success`() = runTest {
        // Arrange
        val coinId = "nullPrices"
        val chartPeriod = "1d"

        val expectedPrices = listOf(
            BigDecimal("25000.89"),
            BigDecimal("30000.47")
        )

        // Act
        val result = coinChartRepository.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod
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

        val expectedResult = Result.Error<CoinChartApiModel>(
            message = "Unable to fetch coin chart"
        )

        // Act
        val result = coinChartRepository.getCoinChart(
            coinId = coinId,
            chartPeriod = chartPeriod
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }
}

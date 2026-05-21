package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartData
import dev.shorthouse.coinwatch.data.source.remote.model.PastPrice
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.PriceEntry
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneOffset

class CoinChartMapperTest {

    // Class under test
    private val coinChartMapper = CoinChartMapper()

    private val testZone = ZoneOffset.UTC
    private val testToday = LocalDate.of(2023, 11, 15)

    @Test
    fun `When coin chart data is null should return default values`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = null
        )

        val expectedCoinChart = CoinChart(
            currency = Currency.USD,
            priceHistory = persistentListOf(),
            periodPriceChangePercentage = Percentage(null)
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.USD,
            zone = testZone,
            today = testToday
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }

    @Test
    fun `When price change percentage null values should return default percentage values`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = null,
                pastPrices = emptyList()
            )
        )

        val expectedCoinChart = CoinChart(
            currency = Currency.USD,
            priceHistory = persistentListOf(),
            periodPriceChangePercentage = Percentage(null)
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.USD,
            zone = testZone,
            today = testToday
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }

    @Test
    fun `When price change percentage is valid should return valid value`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = "0.123",
                pastPrices = emptyList()
            )
        )

        val expectedCoinChart = CoinChart(
            currency = Currency.USD,
            priceHistory = persistentListOf(),
            periodPriceChangePercentage = Percentage("0.123")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.USD,
            zone = testZone,
            today = testToday
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }

    @Test
    fun `When currency is provided should preserve currency in coin chart`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = "0.123",
                pastPrices = emptyList()
            )
        )

        // Act
        val gbpCoinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.GBP,
            zone = testZone,
            today = testToday
        )
        val eurCoinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.EUR,
            zone = testZone,
            today = testToday
        )

        // Assert
        assertThat(gbpCoinChart.currency).isEqualTo(Currency.GBP)
        assertThat(eurCoinChart.currency).isEqualTo(Currency.EUR)
    }

    @Test
    fun `When past prices list is null should return empty list`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = null,
                pastPrices = null
            )
        )

        val expectedCoinChart = CoinChart(
            currency = Currency.USD,
            priceHistory = persistentListOf(),
            periodPriceChangePercentage = Percentage(null)
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.USD,
            zone = testZone,
            today = testToday
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }

    @Test
    fun `When past prices list has null values should filter out these values`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = "1.24",
                pastPrices = listOf(
                    null,
                    PastPrice("123.45", 1700000000L),
                    null,
                    null
                )
            )
        )

        val expectedCoinChart = CoinChart(
            currency = Currency.USD,
            priceHistory = persistentListOf(
                PriceEntry(BigDecimal("123.45"), 1700000000L, "Yesterday 22:13")
            ),
            periodPriceChangePercentage = Percentage("1.24")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.USD,
            zone = testZone,
            today = testToday
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }

    @Test
    fun `When past prices has null amount should filter out these values`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = "1.24",
                pastPrices = listOf(
                    PastPrice("123.45", 1700000000L),
                    PastPrice(null, 1700003600L),
                    PastPrice(null, 1700007200L)
                )
            )
        )

        val expectedCoinChart = CoinChart(
            currency = Currency.USD,
            priceHistory = persistentListOf(
                PriceEntry(BigDecimal("123.45"), 1700000000L, "Yesterday 22:13")
            ),
            periodPriceChangePercentage = Percentage("1.24")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.USD,
            zone = testZone,
            today = testToday
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }

    @Test
    fun `When past prices has invalid amounts should filter out these values`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = "1.24",
                pastPrices = listOf(
                    PastPrice("123.45", 1700000000L),
                    PastPrice("abc", 1700003600L),
                    PastPrice("123.2x", 1700007200L),
                    PastPrice("     ", 1700010800L),
                    PastPrice("", 1700014400L),
                    PastPrice("-123.45", 1700018000L)
                )
            )
        )

        val expectedCoinChart = CoinChart(
            currency = Currency.USD,
            priceHistory = persistentListOf(
                PriceEntry(BigDecimal("123.45"), 1700000000L, "Yesterday 22:13")
            ),
            periodPriceChangePercentage = Percentage("1.24")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.USD,
            zone = testZone,
            today = testToday
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }

    @Test
    fun `When past price amount has whitespace and commas should remove these`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = "1.24",
                pastPrices = listOf(
                    PastPrice("123,456.78", 1700000000L),
                    PastPrice("  123,456.78", 1700003600L),
                    PastPrice("123,456.78  ", 1700007200L),
                    PastPrice("  123,456.78  ", 1700010800L),
                    PastPrice("  123,456.78  ", 1700014400L)
                )
            )
        )

        val expectedCoinChart = CoinChart(
            currency = Currency.USD,
            priceHistory = persistentListOf(
                PriceEntry(BigDecimal("123456.78"), 1700014400L, "02:13"),
                PriceEntry(BigDecimal("123456.78"), 1700010800L, "01:13"),
                PriceEntry(BigDecimal("123456.78"), 1700007200L, "00:13"),
                PriceEntry(BigDecimal("123456.78"), 1700003600L, "Yesterday 23:13"),
                PriceEntry(BigDecimal("123456.78"), 1700000000L, "Yesterday 22:13")
            ),
            periodPriceChangePercentage = Percentage("1.24")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.USD,
            zone = testZone,
            today = testToday
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }

    @Test
    fun `When coin chart has valid values should return valid expected coin chart`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = "2.92",
                pastPrices = listOf(
                    PastPrice("123.45", 1700000000L),
                    PastPrice("123.46", 1700003600L),
                    PastPrice("123.47", 1700007200L),
                    PastPrice("123.48", 1700010800L),
                    PastPrice("123.49", 1700014400L)
                )
            )
        )

        val expectedCoinChart = CoinChart(
            currency = Currency.USD,
            priceHistory = persistentListOf(
                PriceEntry(BigDecimal("123.49"), 1700014400L, "02:13"),
                PriceEntry(BigDecimal("123.48"), 1700010800L, "01:13"),
                PriceEntry(BigDecimal("123.47"), 1700007200L, "00:13"),
                PriceEntry(BigDecimal("123.46"), 1700003600L, "Yesterday 23:13"),
                PriceEntry(BigDecimal("123.45"), 1700000000L, "Yesterday 22:13")
            ),
            periodPriceChangePercentage = Percentage("2.92")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = Currency.USD,
            zone = testZone,
            today = testToday
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }
}

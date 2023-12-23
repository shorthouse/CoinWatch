package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinChartData
import dev.shorthouse.coinwatch.data.source.remote.model.PastPrice
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

class CoinChartMapperTest {

    // Class under test
    private val coinChartMapper = CoinChartMapper()

    private val defaultCurrency = Currency.USD

    @Test
    fun `When coin chart data is null should return default values`() {
        // Arrange
        val apiModel = CoinChartApiModel(
            coinChartData = null
        )

        val expectedCoinChart = CoinChart(
            prices = persistentListOf(),
            minPrice = Price(null),
            maxPrice = Price(null),
            periodPriceChangePercentage = Percentage(null)
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = defaultCurrency
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
            prices = persistentListOf(),
            minPrice = Price(null),
            maxPrice = Price(null),
            periodPriceChangePercentage = Percentage(null)
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = defaultCurrency
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
            prices = persistentListOf(),
            minPrice = Price(null),
            maxPrice = Price(null),
            periodPriceChangePercentage = Percentage("0.123")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
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
            prices = persistentListOf(),
            minPrice = Price(null),
            maxPrice = Price(null),
            periodPriceChangePercentage = Percentage(null)
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = defaultCurrency
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
                    PastPrice("123.45"),
                    null,
                    null
                )
            )
        )

        val expectedCoinChart = CoinChart(
            prices = persistentListOf(
                BigDecimal("123.45")
            ),
            minPrice = Price("123.45"),
            maxPrice = Price("123.45"),
            periodPriceChangePercentage = Percentage("1.24")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = defaultCurrency
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
                    PastPrice("123.45"),
                    PastPrice(null),
                    PastPrice(null)
                )
            )
        )

        val expectedCoinChart = CoinChart(
            prices = persistentListOf(
                BigDecimal("123.45")
            ),
            minPrice = Price("123.45"),
            maxPrice = Price("123.45"),
            periodPriceChangePercentage = Percentage("1.24")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = defaultCurrency
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
                    PastPrice("123.45"),
                    PastPrice("abc"),
                    PastPrice("123.2x"),
                    PastPrice("     "),
                    PastPrice(""),
                    PastPrice("-123.45")
                )
            )
        )

        val expectedCoinChart = CoinChart(
            prices = persistentListOf(
                BigDecimal("123.45")
            ),
            minPrice = Price("123.45"),
            maxPrice = Price("123.45"),
            periodPriceChangePercentage = Percentage("1.24")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = defaultCurrency
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
                    PastPrice("123,456.78"),
                    PastPrice("  123,456.78"),
                    PastPrice("123,456.78  "),
                    PastPrice("  123,456.78  "),
                    PastPrice("  123,456.78  ")
                )
            )
        )

        val expectedCoinChart = CoinChart(
            prices = persistentListOf(
                BigDecimal("123456.78"),
                BigDecimal("123456.78"),
                BigDecimal("123456.78"),
                BigDecimal("123456.78"),
                BigDecimal("123456.78")
            ),
            minPrice = Price("123456.78"),
            maxPrice = Price("123456.78"),
            periodPriceChangePercentage = Percentage("1.24")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = defaultCurrency
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
                    PastPrice("123.45"),
                    PastPrice("123.46"),
                    PastPrice("123.47"),
                    PastPrice("123.48"),
                    PastPrice("123.49")
                )
            )
        )

        val expectedCoinChart = CoinChart(
            prices = persistentListOf(
                BigDecimal("123.49"),
                BigDecimal("123.48"),
                BigDecimal("123.47"),
                BigDecimal("123.46"),
                BigDecimal("123.45")
            ),
            minPrice = Price("123.45"),
            maxPrice = Price("123.49"),
            periodPriceChangePercentage = Percentage("2.92")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = defaultCurrency
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }

    @Test
    fun `When coin chart has gbp currency with valid values should return expected coin chart`() {
        // Arrange
        val currency = Currency.GBP

        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = "2.92",
                pastPrices = listOf(
                    PastPrice("123.45"),
                    PastPrice("123.46"),
                    PastPrice("123.47"),
                    PastPrice("123.48"),
                    PastPrice("123.49")
                )
            )
        )

        val expectedCoinChart = CoinChart(
            prices = persistentListOf(
                BigDecimal("123.49"),
                BigDecimal("123.48"),
                BigDecimal("123.47"),
                BigDecimal("123.46"),
                BigDecimal("123.45")
            ),
            minPrice = Price("123.45", currency = currency),
            maxPrice = Price("123.49", currency = currency),
            periodPriceChangePercentage = Percentage("2.92")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = currency
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }

    @Test
    fun `When coin chart has eur currency with valid values should return expected coin chart`() {
        // Arrange
        val currency = Currency.EUR

        val apiModel = CoinChartApiModel(
            coinChartData = CoinChartData(
                priceChangePercentage = "2.92",
                pastPrices = listOf(
                    PastPrice("123.45"),
                    PastPrice("123.46"),
                    PastPrice("123.47"),
                    PastPrice("123.48"),
                    PastPrice("123.49")
                )
            )
        )

        val expectedCoinChart = CoinChart(
            prices = persistentListOf(
                BigDecimal("123.49"),
                BigDecimal("123.48"),
                BigDecimal("123.47"),
                BigDecimal("123.46"),
                BigDecimal("123.45")
            ),
            minPrice = Price("123.45", currency = currency),
            maxPrice = Price("123.49", currency = currency),
            periodPriceChangePercentage = Percentage("2.92")
        )

        // Act
        val coinChart = coinChartMapper.mapApiModelToModel(
            apiModel = apiModel,
            currency = currency
        )

        // Assert
        assertThat(coinChart).isEqualTo(expectedCoinChart)
    }
}

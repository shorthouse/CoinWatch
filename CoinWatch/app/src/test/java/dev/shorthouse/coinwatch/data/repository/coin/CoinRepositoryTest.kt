package dev.shorthouse.coinwatch.data.repository.coin

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.remote.FakeCoinApi
import dev.shorthouse.coinwatch.data.source.remote.FakeCoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import java.math.BigDecimal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoinRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var coinRepository: CoinRepository

    @Before
    fun setup() {
        coinRepository = CoinRepositoryImpl(
            coinNetworkDataSource = FakeCoinNetworkDataSource(
                coinApi = FakeCoinApi()
            ),
            ioDispatcher = mainDispatcherRule.testDispatcher
        )
    }

    @Test
    fun `getCoins success returns success result`() = runTest {
        // Arrange
        val expectedResult = Result.Success(
            listOf(
                Coin(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29490.954785191607"),
                    priceChangePercentage24h = Percentage("-0.96"),
                    prices24h = listOf(
                        BigDecimal("29790.15810429195"),
                        BigDecimal("29782.07714670252"),
                        BigDecimal("29436.47984833588"),
                        BigDecimal("29510.92753539824"),
                        BigDecimal("29482.564008512305")
                    )
                )
            )
        )

        // Act
        val result = coinRepository.getCoins(
            currencyUUID = "USD"
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `getCoins null values, populates with default values and returns success`() = runTest {
        // Arrange
        val expectedResult = Result.Success(
            listOf(
                Coin(
                    id = "Qwsogvtv82FCd",
                    symbol = "",
                    name = "",
                    imageUrl = "",
                    currentPrice = Price(null),
                    priceChangePercentage24h = Percentage(null),
                    prices24h = emptyList()
                )
            )
        )

        // Act
        val result = coinRepository.getCoins(
            currencyUUID = "nullValues"
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `getCoins null coins list returns empty list success result`() = runTest {
        // Arrange
        val expectedResult = Result.Success(
            emptyList<Coin>()
        )

        // Act
        val result = coinRepository.getCoins(
            currencyUUID = "nullCoins"
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `getCoins coin with null id excluded from list returns success result`() = runTest {
        // Arrange
        val expectedResult = Result.Success(
            listOf(
                Coin(
                    id = "razxDUgYGNAdQ",
                    name = "Ethereum",
                    symbol = "ETH",
                    imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                    currentPrice = Price("1845.7097788177032"),
                    priceChangePercentage24h = Percentage("0.42"),
                    prices24h = listOf(
                        BigDecimal("1857.0635686120618"),
                        BigDecimal("1852.7243420201132"),
                        BigDecimal("1850.8054635160697"),
                        BigDecimal("1848.197142458803"),
                        BigDecimal("1847.2140162508354")
                    )
                )
            )
        )

        // Act
        val result = coinRepository.getCoins(
            currencyUUID = "nullIds"
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `getCoins error returns error result`() = runTest {
        // Arrange
        val expectedResult = Result.Error<CoinsApiModel>(
            message = "Unable to fetch coins list"
        )

        // Act
        val result = coinRepository.getCoins(
            currencyUUID = ""
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `getCoins null retrofit body returns error result`() = runTest {
        // Arrange
        val expectedResult = Result.Error<CoinsApiModel>(
            message = "Unable to fetch coins list"
        )

        // Act
        val result = coinRepository.getCoins(
            currencyUUID = "nullBody"
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `getCoins thrown exception returns error result`() = runTest {
        // Arrange
        val expectedResult = Result.Error<CoinsApiModel>(
            message = "Unable to fetch coins list"
        )

        // Act
        val result = coinRepository.getCoins(
            currencyUUID = "exception"
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }
}

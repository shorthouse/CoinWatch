package dev.shorthouse.coinwatch.data.repository.coin

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsData
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class CoinRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var coinRepository: CoinRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coinRepository = CoinRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinMapper = CoinMapper(),
            ioDispatcher = mainDispatcherRule.testDispatcher
        )
    }

    @Test
    fun `When coins data is valid should return success`() = runTest {
        // Arrange
        val coinSort = CoinSort.MarketCap
        val currency = Currency.USD

        val expectedResult = Result.Success(
            listOf(
                Coin(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29490.954785191607"),
                    priceChangePercentage24h = Percentage("-0.96"),
                    prices24h = persistentListOf(
                        BigDecimal("29790.15810429195"),
                        BigDecimal("29782.07714670252"),
                        BigDecimal("29436.47984833588"),
                        BigDecimal("29510.92753539824"),
                        BigDecimal("29482.564008512305")
                    )
                )
            )
        )

        coEvery {
            coinNetworkDataSource.getCoins(coinSort = coinSort, currency = currency)
        } returns Response.success(
            CoinsApiModel(
                coinsData = CoinsData(
                    coins = listOf(
                        CoinApiModel(
                            id = "Qwsogvtv82FCd",
                            symbol = "BTC",
                            name = "Bitcoin",
                            imageUrl =
                            "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                            currentPrice = "29490.954785191607",
                            priceChangePercentage24h = "-0.96",
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
            )
        )

        // Act
        val result = coinRepository.getCoins(
            coinSort = CoinSort.MarketCap,
            currency = Currency.USD
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When coins data has null values should populate these with default values and return success`() =
        runTest {
            // Arrange
            val coinSort = CoinSort.MarketCap
            val currency = Currency.USD

            val expectedResult = Result.Success(
                listOf(
                    Coin(
                        id = "Qwsogvtv82FCd",
                        symbol = "",
                        name = "",
                        imageUrl = "",
                        currentPrice = Price(null),
                        priceChangePercentage24h = Percentage(null),
                        prices24h = persistentListOf()
                    )
                )
            )

            coEvery {
                coinNetworkDataSource.getCoins(coinSort = coinSort, currency = currency)
            } returns Response.success(
                CoinsApiModel(
                    coinsData = CoinsData(
                        coins = listOf(
                            CoinApiModel(
                                id = "Qwsogvtv82FCd",
                                symbol = null,
                                name = null,
                                imageUrl = null,
                                currentPrice = null,
                                priceChangePercentage24h = null,
                                prices24h = null
                            )
                        )
                    )
                )
            )

            // Act
            val result = coinRepository.getCoins(
                coinSort = CoinSort.MarketCap,
                currency = Currency.USD
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
        }

    @Test
    fun `When coins data is null should return empty list`() = runTest {
        // Arrange
        val coinSort = CoinSort.MarketCap
        val currency = Currency.USD

        val expectedResult = Result.Success<List<Coin>>(emptyList())

        coEvery {
            coinNetworkDataSource.getCoins(coinSort = coinSort, currency = currency)
        } returns Response.success(
            CoinsApiModel(
                coinsData = CoinsData(
                    coins = null
                )
            )
        )

        // Act
        val result = coinRepository.getCoins(
            coinSort = CoinSort.MarketCap,
            currency = Currency.USD
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When coins data has null id coin should filter this out from the list and return success`() =
        runTest {
            // Arrange
            val coinSort = CoinSort.MarketCap
            val currency = Currency.USD

            val expectedResult = Result.Success(
                listOf(
                    Coin(
                        id = "razxDUgYGNAdQ",
                        name = "Ethereum",
                        symbol = "ETH",
                        imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                        currentPrice = Price("1845.7097788177032"),
                        priceChangePercentage24h = Percentage("0.42"),
                        prices24h = persistentListOf(
                            BigDecimal("1857.0635686120618"),
                            BigDecimal("1852.7243420201132"),
                            BigDecimal("1850.8054635160697"),
                            BigDecimal("1848.197142458803"),
                            BigDecimal("1847.2140162508354")
                        )
                    )
                )
            )

            coEvery {
                coinNetworkDataSource.getCoins(coinSort = coinSort, currency = currency)
            } returns Response.success(
                CoinsApiModel(
                    coinsData = CoinsData(
                        coins = listOf(
                            CoinApiModel(
                                id = null,
                                symbol = "BTC",
                                name = "Bitcoin",
                                imageUrl =
                                "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                                currentPrice = "29490.954785191607",
                                priceChangePercentage24h = "-0.96",
                                prices24h = listOf(
                                    BigDecimal("29790.15810429195"),
                                    BigDecimal("29782.07714670252"),
                                    BigDecimal("29436.47984833588"),
                                    BigDecimal("29510.92753539824"),
                                    BigDecimal("29482.564008512305")
                                )
                            ),
                            CoinApiModel(
                                id = "razxDUgYGNAdQ",
                                symbol = "ETH",
                                name = "Ethereum",
                                imageUrl = "https://cdn.coinranking.com/rk4RKHOuW/eth.svg",
                                currentPrice = "1845.7097788177032",
                                priceChangePercentage24h = "0.42",
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
                )
            )

            // Act
            val result = coinRepository.getCoins(
                coinSort = CoinSort.MarketCap,
                currency = Currency.USD
            ).first()

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
        }

    @Test
    fun `When coins returns null retrofit body should return error`() = runTest {
        // Arrange
        val coinSort = CoinSort.MarketCap
        val currency = Currency.USD

        val expectedResult = Result.Error<CoinsApiModel>(
            message = "Unable to fetch coins list"
        )

        coEvery {
            coinNetworkDataSource.getCoins(coinSort = coinSort, currency = currency)
        } returns Response.success(null)

        // Act
        val result = coinRepository.getCoins(
            coinSort = CoinSort.MarketCap,
            currency = Currency.USD
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When coins returns error should return error`() = runTest {
        // Arrange
        val coinSort = CoinSort.MarketCap
        val currency = Currency.USD

        val expectedResult = Result.Error<CoinsApiModel>(
            message = "Unable to fetch coins list"
        )

        coEvery {
            coinNetworkDataSource.getCoins(coinSort = coinSort, currency = currency)
        } returns Response.error(
            404,
            "Coins not found".toResponseBody(null)
        )

        // Act
        val result = coinRepository.getCoins(
            coinSort = CoinSort.MarketCap,
            currency = Currency.USD
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When coins throws exception should return error`() = runTest {
        // Arrange
        val coinSort = CoinSort.MarketCap
        val currency = Currency.USD

        val expectedResult = Result.Error<CoinsApiModel>(
            message = "Unable to fetch coins list"
        )

        coEvery {
            coinNetworkDataSource.getCoins(coinSort = coinSort, currency = currency)
        } throws IOException("Coins not found")

        // Act
        val result = coinRepository.getCoins(
            coinSort = CoinSort.MarketCap,
            currency = Currency.USD
        ).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }
}

package dev.shorthouse.coinwatch.data.repository.coin

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinMapper
import dev.shorthouse.coinwatch.data.source.local.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.model.CachedCoin
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsData
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.unmockkAll
import java.io.IOException
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class CachedCoinRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var cachedCoinRepository: CachedCoinRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @MockK
    private lateinit var coinLocalDataSource: CoinLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        cachedCoinRepository = CachedCoinRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinLocalDataSource = coinLocalDataSource,
            coinMapper = CoinMapper(),
            ioDispatcher = mainDispatcherRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When remote coins returns valid data should return success`() = runTest {
        // Arrange
        val coinSort = CoinSort.MarketCap
        val currency = Currency.EUR

        coEvery {
            coinNetworkDataSource.getCoins(
                coinSort = coinSort,
                currency = currency
            )
        } returns Response.success(
            CoinsApiModel(
                coinsData = CoinsData(
                    coins = listOf(
                        CoinApiModel(
                            id = "Qwsogvtv82FCd",
                            symbol = "BTC",
                            name = "Bitcoin",
                            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                            currentPrice = "29490.954785191607",
                            priceChangePercentage24h = "-0.96",
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
            )
        )

        val expectedResult = Result.Success(
            listOf(
                CachedCoin(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29490.954785191607", currency = currency),
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

        // Act
        val result = cachedCoinRepository.getRemoteCoins(
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When remote coins data has null values should populate with defaults and return success`() =
        runTest {
            // Arrange
            val coinSort = CoinSort.MarketCap
            val currency = Currency.USD

            coEvery {
                coinNetworkDataSource.getCoins(
                    coinSort = coinSort,
                    currency = currency
                )
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

            val expectedResult = Result.Success(
                listOf(
                    CachedCoin(
                        id = "Qwsogvtv82FCd",
                        name = "",
                        symbol = "",
                        imageUrl = "",
                        currentPrice = Price(null, currency = currency),
                        priceChangePercentage24h = Percentage(null),
                        prices24h = persistentListOf()
                    )
                )
            )

            // Act
            val result = cachedCoinRepository.getRemoteCoins(
                coinSort = coinSort,
                currency = currency
            )

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
        }

    @Test
    fun `When coins data is null should return error`() = runTest {
        // Arrange
        val coinSort = CoinSort.MarketCap
        val currency = Currency.USD

        coEvery {
            coinNetworkDataSource.getCoins(
                coinSort = coinSort,
                currency = currency
            )
        } returns Response.success(
            CoinsApiModel(
                coinsData = null
            )
        )

        val expectedResult = Result.Error<List<CachedCoin>>(
            message = "Unable to fetch network coins list"
        )

        // Act
        val result = cachedCoinRepository.getRemoteCoins(
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When retrofit response is unsuccessful should return error`() = runTest {
        // Arrange
        val coinSort = CoinSort.MarketCap
        val currency = Currency.USD
        val errorMessage = "Unable to fetch coin search results"

        coEvery {
            coinNetworkDataSource.getCoins(
                coinSort = coinSort,
                currency = currency
            )
        } returns Response.error(
            404,
            errorMessage.toResponseBody(null)
        )

        val expectedResult = Result.Error<List<CachedCoin>>(
            message = "Unable to fetch network coins list"
        )

        // Act
        val result = cachedCoinRepository.getRemoteCoins(
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When mapping remote coins throws error should catch and return error`() = runTest {
        // Arrange
        val coinSort = CoinSort.MarketCap
        val currency = Currency.USD

        coEvery {
            coinNetworkDataSource.getCoins(
                coinSort = coinSort,
                currency = currency
            )
        } throws IOException()

        val expectedResult = Result.Error<List<CachedCoin>>(
            message = "Unable to fetch network coins list"
        )

        // Act
        val result = cachedCoinRepository.getRemoteCoins(
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When get cached coins with valid data should return success`() = runTest {
        // Arrange
        every { coinLocalDataSource.getCoins() } returns flowOf(
            listOf(
                CachedCoin(
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

        val expectedResult = Result.Success(
            listOf(
                CachedCoin(
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

        // Act
        val result = cachedCoinRepository.getCachedCoins().first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When refreshing cached coins should call refresh cache coins`() = runTest {
        // Arrange
        val coins = listOf(
            CachedCoin(
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

        coEvery { coinLocalDataSource.updateCoins(coins) } just runs

        // Act
        cachedCoinRepository.refreshCachedCoins(coins)

        // Assert
        coVerifySequence {
            coinLocalDataSource.updateCoins(coins)
        }
    }
}

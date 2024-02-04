package dev.shorthouse.coinwatch.data.repository.coin

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinMapper
import dev.shorthouse.coinwatch.data.source.local.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.model.Coin
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class CoinRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var coinRepository: CoinRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @MockK
    private lateinit var coinLocalDataSource: CoinLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coinRepository = CoinRepositoryImpl(
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
                        )
                    )
                )
            )
        )

        val expectedResult = Result.Success(
            listOf(
                Coin(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29490.954785191607", currency = currency),
                    priceChangePercentage24h = Percentage("-0.96"),
                )
            )
        )

        // Act
        val result = coinRepository.getRemoteCoins(
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
                            )
                        )
                    )
                )
            )

            val expectedResult = Result.Success(
                listOf(
                    Coin(
                        id = "Qwsogvtv82FCd",
                        name = "",
                        symbol = "",
                        imageUrl = "",
                        currentPrice = Price(null, currency = currency),
                        priceChangePercentage24h = Percentage(null),
                    )
                )
            )

            // Act
            val result = coinRepository.getRemoteCoins(
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

        val expectedResult = Result.Error<List<Coin>>(
            message = "Unable to fetch network coins list"
        )

        // Act
        val result = coinRepository.getRemoteCoins(
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

        val expectedResult = Result.Error<List<Coin>>(
            message = "Unable to fetch network coins list"
        )

        // Act
        val result = coinRepository.getRemoteCoins(
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

        val expectedResult = Result.Error<List<Coin>>(
            message = "Unable to fetch network coins list"
        )

        // Act
        val result = coinRepository.getRemoteCoins(
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
                Coin(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29490.954785191607"),
                    priceChangePercentage24h = Percentage("-0.96"),
                )
            )
        )

        val expectedResult = Result.Success(
            listOf(
                Coin(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29490.954785191607"),
                    priceChangePercentage24h = Percentage("-0.96"),
                )
            )
        )

        // Act
        val result = coinRepository.getCachedCoins().first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When refreshing cached coins should call refresh cache coins`() = runTest {
        // Arrange
        val coins = listOf(
            Coin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29490.954785191607"),
                priceChangePercentage24h = Percentage("-0.96"),
            )
        )

        coEvery { coinLocalDataSource.updateCoins(coins) } just runs

        // Act
        coinRepository.updateCachedCoins(coins)

        // Assert
        coVerifySequence {
            coinLocalDataSource.updateCoins(coins)
        }
    }
}

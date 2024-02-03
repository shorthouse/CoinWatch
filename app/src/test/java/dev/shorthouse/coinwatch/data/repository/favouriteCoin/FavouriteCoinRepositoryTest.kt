package dev.shorthouse.coinwatch.data.repository.favouriteCoin

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.FavouriteCoinMapper
import dev.shorthouse.coinwatch.data.source.local.CoinLocalDataSource
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsData
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

class FavouriteCoinRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var favouriteCoinRepository: FavouriteCoinRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @MockK
    private lateinit var coinLocalDataSource: CoinLocalDataSource

    private val favouriteCoinMapper = FavouriteCoinMapper()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        favouriteCoinRepository = FavouriteCoinRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinLocalDataSource = coinLocalDataSource,
            favouriteCoinMapper = favouriteCoinMapper,
            ioDispatcher = mainDispatcherRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When remote coins with empty coins ids should return success with empty list`() = runTest {
        // Arrange
        val coinIds = emptyList<String>()
        val coinSort = CoinSort.MarketCap
        val currency = Currency.EUR

        val expectedResult = Result.Success<List<FavouriteCoin>>(
            emptyList()
        )

        // Act
        val result = favouriteCoinRepository.getRemoteFavouriteCoins(
            coinIds = coinIds,
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)

        coVerifySequence(inverse = true) {
            coinNetworkDataSource.getFavouriteCoins(
                coinIds = coinIds,
                coinSort = coinSort,
                currency = currency
            )
        }
    }

    @Test
    fun `When remote coins returns retrofit error should return error`() = runTest {
        // Arrange
        val coinIds = listOf("Qwsogvtv82FCd")
        val coinSort = CoinSort.MarketCap
        val currency = Currency.EUR
        val errorMessage = "Unable to fetch network favourite coins list"

        coEvery {
            coinNetworkDataSource.getFavouriteCoins(
                coinIds = coinIds,
                coinSort = coinSort,
                currency = currency
            )
        } returns Response.error(
            404,
            errorMessage.toResponseBody(null)
        )

        val expectedResult = Result.Error<List<FavouriteCoin>>(
            message = errorMessage
        )

        // Act
        val result = favouriteCoinRepository.getRemoteFavouriteCoins(
            coinIds = coinIds,
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When remote favourite coins api model is null should return error`() = runTest {
        // Arrange
        val coinIds = listOf("Qwsogvtv82FCd")
        val coinSort = CoinSort.MarketCap
        val currency = Currency.EUR
        val errorMessage = "Unable to fetch network favourite coins list"

        coEvery {
            coinNetworkDataSource.getFavouriteCoins(
                coinIds = coinIds,
                coinSort = coinSort,
                currency = currency
            )
        } returns Response.success(
            null
        )

        val expectedResult = Result.Error<List<FavouriteCoin>>(
            message = errorMessage
        )

        // Act
        val result = favouriteCoinRepository.getRemoteFavouriteCoins(
            coinIds = coinIds,
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When remote favourite coins data is null should return error`() = runTest {
        // Arrange
        val coinIds = listOf("Qwsogvtv82FCd")
        val coinSort = CoinSort.MarketCap
        val currency = Currency.EUR
        val errorMessage = "Unable to fetch network favourite coins list"

        coEvery {
            coinNetworkDataSource.getFavouriteCoins(
                coinIds = coinIds,
                coinSort = coinSort,
                currency = currency
            )
        } returns Response.success(
            FavouriteCoinsApiModel(
                favouriteCoinsData = null
            )
        )

        val expectedResult = Result.Error<List<FavouriteCoin>>(
            message = errorMessage
        )

        // Act
        val result = favouriteCoinRepository.getRemoteFavouriteCoins(
            coinIds = coinIds,
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When remote favourite coins is null should return error`() = runTest {
        // Arrange
        val coinIds = listOf("Qwsogvtv82FCd")
        val coinSort = CoinSort.MarketCap
        val currency = Currency.EUR
        val errorMessage = "Unable to fetch network favourite coins list"

        coEvery {
            coinNetworkDataSource.getFavouriteCoins(
                coinIds = coinIds,
                coinSort = coinSort,
                currency = currency
            )
        } returns Response.success(
            FavouriteCoinsApiModel(
                favouriteCoinsData = FavouriteCoinsData(
                    favouriteCoins = null
                )
            )
        )

        val expectedResult = Result.Error<List<FavouriteCoin>>(
            message = errorMessage
        )

        // Act
        val result = favouriteCoinRepository.getRemoteFavouriteCoins(
            coinIds = coinIds,
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When get remote favourite coins throws should catch and return error`() = runTest {
        // Arrange
        val coinIds = listOf("Qwsogvtv82FCd")
        val coinSort = CoinSort.MarketCap
        val currency = Currency.EUR
        val errorMessage = "Unable to fetch network favourite coins list"

        coEvery {
            coinNetworkDataSource.getFavouriteCoins(
                coinIds = coinIds,
                coinSort = coinSort,
                currency = currency
            )
        } throws IOException(errorMessage)

        val expectedResult = Result.Error<List<FavouriteCoin>>(
            message = errorMessage
        )

        // Act
        val result = favouriteCoinRepository.getRemoteFavouriteCoins(
            coinIds = coinIds,
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When remote favourite coins is successful should return success`() = runTest {
        // Arrange
        val coinIds = listOf("Qwsogvtv82FCd")
        val coinSort = CoinSort.MarketCap
        val currency = Currency.EUR

        coEvery {
            coinNetworkDataSource.getFavouriteCoins(
                coinIds = coinIds,
                coinSort = coinSort,
                currency = currency
            )
        } returns Response.success(
            FavouriteCoinsApiModel(
                favouriteCoinsData = FavouriteCoinsData(
                    favouriteCoins = listOf(
                        FavouriteCoinApiModel(
                            id = "Qwsogvtv82FCd",
                            name = "Bitcoin",
                            symbol = "BTC",
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
                FavouriteCoin(
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
        val result = favouriteCoinRepository.getRemoteFavouriteCoins(
            coinIds = coinIds,
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When getting cached favourite coins with valid data should return success`() = runTest {
        // Arrange
        every { coinLocalDataSource.getFavouriteCoins() } returns flowOf(
            listOf(
                FavouriteCoin(
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
                FavouriteCoin(
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
        val result = favouriteCoinRepository.getCachedFavouriteCoins().first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When updating cached favourite coin should call update favourite coins`() = runTest {
        // Arrange
        val favouriteCoins = listOf(
            FavouriteCoin(
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

        coEvery { coinLocalDataSource.updateFavouriteCoins(favouriteCoins) } just runs

        // Act
        favouriteCoinRepository.updateCachedFavouriteCoins(favouriteCoins)

        // Assert
        coVerifySequence {
            coinLocalDataSource.updateFavouriteCoins(favouriteCoins)
        }
    }
}

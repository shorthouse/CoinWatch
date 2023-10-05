package dev.shorthouse.coinwatch.data.repository.searchresults

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinSearchResultsMapper
import dev.shorthouse.coinwatch.data.repository.searchResults.CoinSearchResultsRepository
import dev.shorthouse.coinwatch.data.repository.searchResults.CoinSearchResultsRepositoryImpl
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResult
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsData
import dev.shorthouse.coinwatch.model.SearchCoin
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class CoinSearchResultsRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var coinSearchResultsRepository: CoinSearchResultsRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coinSearchResultsRepository = CoinSearchResultsRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            coinSearchResultsMapper = CoinSearchResultsMapper(),
            ioDispatcher = mainDispatcherRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When coin search results data is valid should return success`() = runTest {
        // Arrange
        val searchQuery = "bit"

        val expectedResult = Result.Success(
            listOf(
                SearchCoin(
                    id = "Qwsogvtv82FCd",
                    symbol = "BTC",
                    name = "Bitcoin",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg"
                ),
                SearchCoin(
                    id = "ZlZpzOJo43mIo",
                    symbol = "BCH",
                    name = "Bitcoin Cash",
                    imageUrl = "https://cdn.coinranking.com/By8ziihX7/bch.svg"
                )
            )
        )

        coEvery {
            coinNetworkDataSource.getCoinSearchResults(searchQuery)
        } returns
            Response.success(
                CoinSearchResultsApiModel(
                    coinsSearchResultsData = CoinSearchResultsData(
                        coinSearchResults = listOf(
                            CoinSearchResult(
                                id = "Qwsogvtv82FCd",
                                symbol = "BTC",
                                name = "Bitcoin",
                                imageUrl =
                                "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                                currentPrice = "29490.954785191607"
                            ),
                            CoinSearchResult(
                                id = "ZlZpzOJo43mIo",
                                symbol = "BCH",
                                name = "Bitcoin Cash",
                                imageUrl = "https://cdn.coinranking.com/By8ziihX7/bch.svg",
                                currentPrice = "228.2807353007971"
                            )
                        )
                    )
                )
            )

        // Act
        val result = coinSearchResultsRepository.getCoinSearchResults(searchQuery)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When coin search results data has null values should populate these with default values and return success`() =
        runTest {
            // Arrange
            val searchQuery = ""

            val expectedResult = Result.Success(
                listOf(
                    SearchCoin(
                        id = "Qwsogvtv82FCd",
                        symbol = "",
                        name = "",
                        imageUrl = ""
                    )
                )
            )

            coEvery {
                coinNetworkDataSource.getCoinSearchResults(searchQuery)
            } returns
                Response.success(
                    CoinSearchResultsApiModel(
                        coinsSearchResultsData = CoinSearchResultsData(
                            coinSearchResults = listOf(
                                CoinSearchResult(
                                    id = "Qwsogvtv82FCd",
                                    symbol = null,
                                    name = null,
                                    imageUrl = null,
                                    currentPrice = null
                                )
                            )
                        )
                    )
                )

            // Act
            val result = coinSearchResultsRepository.getCoinSearchResults(searchQuery)

            // Assert
            assertThat(result).isInstanceOf(Result.Success::class.java)
            assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
        }

    @Test
    fun `When coin id is null should filter from list and return success`() = runTest {
        // Arrange
        val searchQuery = ""

        val expectedResult = Result.Success<List<SearchCoin>>(
            emptyList()
        )

        coEvery {
            coinNetworkDataSource.getCoinSearchResults(searchQuery)
        } returns
            Response.success(
                CoinSearchResultsApiModel(
                    coinsSearchResultsData = CoinSearchResultsData(
                        coinSearchResults = listOf(
                            CoinSearchResult(
                                id = null,
                                symbol = null,
                                name = null,
                                imageUrl = null,
                                currentPrice = null
                            )
                        )
                    )
                )
            )

        // Act
        val result = coinSearchResultsRepository.getCoinSearchResults(searchQuery)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When coin search results is empty should return success with empty list`() = runTest {
        // Arrange
        val searchQuery = ""

        val expectedResult = Result.Success<List<SearchCoin>>(
            emptyList()
        )

        coEvery {
            coinNetworkDataSource.getCoinSearchResults(searchQuery)
        } returns
            Response.success(
                CoinSearchResultsApiModel(
                    coinsSearchResultsData = CoinSearchResultsData(
                        coinSearchResults = emptyList()
                    )
                )
            )

        // Act
        val result = coinSearchResultsRepository.getCoinSearchResults(searchQuery)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When coin search results is null should return success with empty list`() = runTest {
        // Arrange
        val searchQuery = ""

        val expectedResult = Result.Success<List<SearchCoin>>(
            emptyList()
        )

        coEvery {
            coinNetworkDataSource.getCoinSearchResults(searchQuery)
        } returns
            Response.success(
                CoinSearchResultsApiModel(
                    coinsSearchResultsData = CoinSearchResultsData(
                        coinSearchResults = null
                    )
                )
            )

        // Act
        val result = coinSearchResultsRepository.getCoinSearchResults(searchQuery)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When coin search results data is null should return error`() = runTest {
        // Arrange
        val searchQuery = ""
        val errorMessage = "Unable to fetch coin search results"

        val expectedResult = Result.Error<List<SearchCoin>>(
            message = errorMessage
        )

        coEvery {
            coinNetworkDataSource.getCoinSearchResults(searchQuery)
        } returns
            Response.success(
                CoinSearchResultsApiModel(
                    coinsSearchResultsData = null
                )
            )

        // Act
        val result = coinSearchResultsRepository.getCoinSearchResults(searchQuery)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When coin search results api model is null should return error`() = runTest {
        // Arrange
        val searchQuery = ""
        val errorMessage = "Unable to fetch coin search results"

        val expectedResult = Result.Error<List<SearchCoin>>(
            message = errorMessage
        )

        coEvery {
            coinNetworkDataSource.getCoinSearchResults(searchQuery)
        } returns
            Response.success(
                null
            )

        // Act
        val result = coinSearchResultsRepository.getCoinSearchResults(searchQuery)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When coin search results has unsuccessful response should return error`() = runTest {
        // Arrange
        val searchQuery = ""
        val errorMessage = "Unable to fetch coin search results"

        val expectedResult = Result.Error<List<SearchCoin>>(
            message = errorMessage
        )

        coEvery {
            coinNetworkDataSource.getCoinSearchResults(searchQuery)
        } returns
            Response.error(
                404,
                errorMessage.toResponseBody(null)
            )

        // Act
        val result = coinSearchResultsRepository.getCoinSearchResults(searchQuery)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When coin search results throws should return error`() = runTest {
        // Arrange
        val searchQuery = ""
        val errorMessage = "Unable to fetch coin search results"

        val expectedResult = Result.Error<List<SearchCoin>>(
            message = errorMessage
        )

        coEvery {
            coinNetworkDataSource.getCoinSearchResults(searchQuery)
        } throws IllegalArgumentException(errorMessage)

        // Act
        val result = coinSearchResultsRepository.getCoinSearchResults(searchQuery)

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }
}

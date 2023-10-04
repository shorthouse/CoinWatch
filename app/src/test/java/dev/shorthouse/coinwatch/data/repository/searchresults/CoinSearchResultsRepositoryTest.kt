package dev.shorthouse.coinwatch.data.repository.searchresults

import com.google.common.truth.Truth
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.CoinSearchResultsMapper
import dev.shorthouse.coinwatch.data.repository.searchResults.CoinSearchResultsRepository
import dev.shorthouse.coinwatch.data.repository.searchResults.CoinSearchResultsRepositoryImpl
import dev.shorthouse.coinwatch.data.source.remote.FakeCoinApi
import dev.shorthouse.coinwatch.data.source.remote.FakeCoinNetworkDataSource
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.SearchCoin
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoinSearchResultsRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var coinSearchResultsRepository: CoinSearchResultsRepository

    @Before
    fun setup() {
        coinSearchResultsRepository = CoinSearchResultsRepositoryImpl(
            coinNetworkDataSource = FakeCoinNetworkDataSource(
                coinApi = FakeCoinApi()
            ),
            coinSearchResultsMapper = CoinSearchResultsMapper(),
            ioDispatcher = mainDispatcherRule.testDispatcher
        )
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
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29490.954785191607")
                ),
                SearchCoin(
                    id = "ZlZpzOJo43mIo",
                    symbol = "BCH",
                    name = "Bitcoin Cash",
                    imageUrl = "https://cdn.coinranking.com/By8ziihX7/bch.svg",
                    currentPrice = Price("228.2807353007971")
                )
            )
        )

        // Act
        val result = coinSearchResultsRepository.getCoinSearchResults(searchQuery)

        // Assert
        Truth.assertThat(result).isInstanceOf(Result.Success::class.java)
        Truth.assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }
}

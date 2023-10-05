package dev.shorthouse.coinwatch.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResult
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.CoinSearchResultsData
import dev.shorthouse.coinwatch.model.SearchCoin
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CoinSearchResultsMapperTest {

    // Class under test
    private val coinSearchResultsMapper = CoinSearchResultsMapper()

    @Test
    fun `When coinsSearchResultsData null should return empty list`() = runTest {
        // Arrange
        val apiModel = CoinSearchResultsApiModel(
            coinsSearchResultsData = null
        )

        // Act
        val coinSearchResults = coinSearchResultsMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coinSearchResults).isEmpty()
    }

    @Test
    fun `When coinSearchResults null should return empty list`() = runTest {
        // Arrange
        val apiModel = CoinSearchResultsApiModel(
            coinsSearchResultsData = CoinSearchResultsData(
                coinSearchResults = null
            )
        )

        // Act
        val coinSearchResults = coinSearchResultsMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coinSearchResults).isEmpty()
    }

    @Test
    fun `When search results has null list items should filter out these items`() = runTest {
        // Arrange
        val apiModel = CoinSearchResultsApiModel(
            coinsSearchResultsData = CoinSearchResultsData(
                coinSearchResults = listOf(
                    null,
                    null,
                    CoinSearchResult(
                        id = "Qwsogvtv82FCd",
                        symbol = "BTC",
                        name = "Bitcoin",
                        imageUrl =
                        "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29490.954785191607"
                    ),
                    null
                )
            )
        )

        val expectedCoinSearchResults = listOf(
            SearchCoin(
                id = "Qwsogvtv82FCd",
                symbol = "BTC",
                name = "Bitcoin",
                imageUrl =
                "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg"
            )
        )

        // Act
        val coinSearchResults = coinSearchResultsMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coinSearchResults).isEqualTo(expectedCoinSearchResults)
    }

    @Test
    fun `When search results has null id items should filter out these items`() = runTest {
        // Arrange
        val apiModel = CoinSearchResultsApiModel(
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
                        id = null,
                        symbol = "BTC",
                        name = "Bitcoin",
                        imageUrl =
                        "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = "29490.954785191607"
                    ),
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

        val expectedCoinSearchResults = listOf(
            SearchCoin(
                id = "Qwsogvtv82FCd",
                symbol = "BTC",
                name = "Bitcoin",
                imageUrl =
                "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg"
            )
        )

        // Act
        val coinSearchResults = coinSearchResultsMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coinSearchResults).isEqualTo(expectedCoinSearchResults)
    }

    @Test
    fun `When search results has null values should replace with default values`() = runTest {
        // Arrange
        val apiModel = CoinSearchResultsApiModel(
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

        val expectedCoinSearchResults = listOf(
            SearchCoin(
                id = "Qwsogvtv82FCd",
                symbol = "",
                name = "",
                imageUrl = ""
            )
        )

        // Act
        val coinSearchResults = coinSearchResultsMapper.mapApiModelToModel(apiModel)

        // Assert
        assertThat(coinSearchResults).isEqualTo(expectedCoinSearchResults)
    }
}

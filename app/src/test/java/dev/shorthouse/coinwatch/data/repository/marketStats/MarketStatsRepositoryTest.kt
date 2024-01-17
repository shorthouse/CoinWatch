package dev.shorthouse.coinwatch.data.repository.marketStats

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.MarketStatsMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsData
import dev.shorthouse.coinwatch.data.source.remote.model.MarketStatsDataHolder
import dev.shorthouse.coinwatch.model.MarketStats
import dev.shorthouse.coinwatch.model.Percentage
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import java.io.IOException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class MarketStatsRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var marketStatsRepository: MarketStatsRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        marketStatsRepository = MarketStatsRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            marketStatsMapper = MarketStatsMapper()
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When market stats data is valid should return success`() = runTest {
        // Arrange
        val expectedResult = Result.Success(
            MarketStats(
                marketCapChangePercentage24h = Percentage("1.234")
            )
        )

        coEvery { coinNetworkDataSource.getMarketStats() } returns Response.success(
            MarketStatsApiModel(
                marketStatsDataHolder = MarketStatsDataHolder(
                    marketStatsData = MarketStatsData(
                        marketCapChangePercentage24h = "1.234"
                    )
                )
            )
        )

        // Act
        val result = marketStatsRepository.getMarketStats()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When market stats data is null should return success with default values`() = runTest {
        // Arrange
        val expectedResult = Result.Success(
            MarketStats(
                marketCapChangePercentage24h = Percentage(null)
            )
        )

        coEvery { coinNetworkDataSource.getMarketStats() } returns Response.success(
            MarketStatsApiModel(
                marketStatsDataHolder = MarketStatsDataHolder(
                    marketStatsData = MarketStatsData(
                        marketCapChangePercentage24h = null
                    )
                )
            )
        )

        // Act
        val result = marketStatsRepository.getMarketStats()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When market stats data is null should return error result`() = runTest {
        // Arrange
        val expectedResult = Result.Error<MarketStats>(
            message = "Unable to fetch market stats"
        )

        coEvery { coinNetworkDataSource.getMarketStats() } returns Response.success(
            MarketStatsApiModel(
                marketStatsDataHolder = MarketStatsDataHolder(
                    marketStatsData = null
                )
            )
        )

        // Act
        val result = marketStatsRepository.getMarketStats()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When market stats data holder is null should return error result`() = runTest {
        // Arrange
        val expectedResult = Result.Error<MarketStats>(
            message = "Unable to fetch market stats"
        )

        coEvery { coinNetworkDataSource.getMarketStats() } returns Response.success(
            MarketStatsApiModel(
                marketStatsDataHolder = null
            )
        )

        // Act
        val result = marketStatsRepository.getMarketStats()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When market stats api model is null should return error result`() = runTest {
        // Arrange
        val expectedResult = Result.Error<MarketStats>(
            message = "Unable to fetch market stats"
        )

        coEvery { coinNetworkDataSource.getMarketStats() } returns Response.success(null)

        // Act
        val result = marketStatsRepository.getMarketStats()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When market stats gives error response should return error result`() = runTest {
        // Arrange
        val expectedResult = Result.Error<MarketStats>(
            message = "Unable to fetch market stats"
        )

        coEvery { coinNetworkDataSource.getMarketStats() } returns Response.error(
            404,
            "Market stats unavailable".toResponseBody(null)
        )

        // Act
        val result = marketStatsRepository.getMarketStats()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }

    @Test
    fun `When market stats throws exception should return error result`() = runTest {
        // Arrange
        val expectedResult = Result.Error<MarketStats>(
            message = "Unable to fetch market stats"
        )

        coEvery { coinNetworkDataSource.getMarketStats() } throws IOException()

        // Act
        val result = marketStatsRepository.getMarketStats()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)
    }
}

package dev.shorthouse.coinwatch.data.repository.feargreed

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.FearGreedMapper
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.FearGreedApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FearGreedDataPointApiModel
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import dev.shorthouse.coinwatch.rule.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import java.io.IOException
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class FearGreedRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fearGreedRepository: FearGreedRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        fearGreedRepository = FearGreedRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            fearGreedMapper = FearGreedMapper()
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When fear greed data is valid should return success`() = runTest {
        val expectedResult = Result.Success(
            FearGreed(
                value = 39,
                moodBand = FearGreedMoodBand.Fear,
                history = persistentListOf(
                    BigDecimal("20"),
                    BigDecimal("31"),
                    BigDecimal("39")
                )
            )
        )

        coEvery { coinNetworkDataSource.getFearGreed() } returns Response.success(
            FearGreedApiModel(
                data = listOf(
                    FearGreedDataPointApiModel(timestamp = 300L, value = 39.3),
                    FearGreedDataPointApiModel(timestamp = 100L, value = 20.1),
                    FearGreedDataPointApiModel(timestamp = 200L, value = 30.6)
                )
            )
        )

        val result = fearGreedRepository.getFearGreed()

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)
    }

    @Test
    fun `When fear greed response body is null should return error`() = runTest {
        coEvery { coinNetworkDataSource.getFearGreed() } returns Response.success(null)

        val result = fearGreedRepository.getFearGreed()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When fear greed response data is null should return error`() = runTest {
        coEvery { coinNetworkDataSource.getFearGreed() } returns Response.success(
            FearGreedApiModel(
                data = null
            )
        )

        val result = fearGreedRepository.getFearGreed()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When fear greed response is unsuccessful should return error`() = runTest {
        coEvery { coinNetworkDataSource.getFearGreed() } returns Response.error(
            422,
            "".toResponseBody()
        )

        val result = fearGreedRepository.getFearGreed()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When fear greed response has no valid data should return error`() = runTest {
        coEvery { coinNetworkDataSource.getFearGreed() } returns Response.success(
            FearGreedApiModel(
                data = listOf(
                    FearGreedDataPointApiModel(timestamp = null, value = 39.3),
                    FearGreedDataPointApiModel(timestamp = 100L, value = null),
                    null
                )
            )
        )

        val result = fearGreedRepository.getFearGreed()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When fear greed request throws exception should return error`() = runTest {
        coEvery { coinNetworkDataSource.getFearGreed() } throws IOException()

        val result = fearGreedRepository.getFearGreed()

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    private companion object {
        const val ERROR_MESSAGE = "Unable to fetch fear and greed"
    }
}

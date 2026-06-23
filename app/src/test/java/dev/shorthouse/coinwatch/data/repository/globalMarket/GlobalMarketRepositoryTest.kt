package dev.shorthouse.coinwatch.data.repository.globalMarket

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.GlobalMarketMapper
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsData
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalMarketCoinStatsDataHolder
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalStatsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.GlobalStatsData
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.rule.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import java.io.IOException
import java.math.BigDecimal
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class GlobalMarketRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var globalMarketRepository: GlobalMarketRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        globalMarketRepository = GlobalMarketRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            globalMarketMapper = GlobalMarketMapper()
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When global market data is valid should return success`() = runTest {
        coEvery { coinNetworkDataSource.getGlobalStats(Currency.EUR) } returns Response.success(
            globalStatsApiModel()
        )
        coEvery {
            coinNetworkDataSource.getGlobalMarketCoinStats(Currency.EUR)
        } returns Response.success(globalMarketCoinStatsApiModel())

        val result = globalMarketRepository.getGlobalMarket(currency = Currency.EUR)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(
            GlobalMarket(
                totalMarketCap = Price("2410000000000", currency = Currency.EUR),
                volume24h = Price("98200000000", currency = Currency.EUR),
                btcDominancePercentage = BigDecimal("54.2"),
                coinsUp24h = 2841,
                coinsDown24h = 1893
            )
        )
    }

    @Test
    fun `When global stats response is unsuccessful should return error`() = runTest {
        coEvery { coinNetworkDataSource.getGlobalStats(Currency.USD) } returns Response.error(
            404,
            "Global stats unavailable".toResponseBody()
        )
        coEvery {
            coinNetworkDataSource.getGlobalMarketCoinStats(Currency.USD)
        } returns Response.success(globalMarketCoinStatsApiModel())

        val result = globalMarketRepository.getGlobalMarket(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When global stats response body is null should return error`() = runTest {
        coEvery { coinNetworkDataSource.getGlobalStats(Currency.USD) } returns Response.success(null)
        coEvery {
            coinNetworkDataSource.getGlobalMarketCoinStats(Currency.USD)
        } returns Response.success(globalMarketCoinStatsApiModel())

        val result = globalMarketRepository.getGlobalMarket(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When global stats required data is missing should return error`() = runTest {
        coEvery { coinNetworkDataSource.getGlobalStats(Currency.USD) } returns Response.success(
            GlobalStatsApiModel(data = null)
        )
        coEvery {
            coinNetworkDataSource.getGlobalMarketCoinStats(Currency.USD)
        } returns Response.success(globalMarketCoinStatsApiModel())

        val result = globalMarketRepository.getGlobalMarket(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When global market coin stats response is unsuccessful should return error`() = runTest {
        coEvery { coinNetworkDataSource.getGlobalStats(Currency.USD) } returns Response.success(
            globalStatsApiModel()
        )
        coEvery { coinNetworkDataSource.getGlobalMarketCoinStats(Currency.USD) } returns
            Response.error(404, "Coin stats unavailable".toResponseBody())

        val result = globalMarketRepository.getGlobalMarket(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When global market coin stats response body is null should return error`() = runTest {
        coEvery { coinNetworkDataSource.getGlobalStats(Currency.USD) } returns Response.success(
            globalStatsApiModel()
        )
        coEvery {
            coinNetworkDataSource.getGlobalMarketCoinStats(Currency.USD)
        } returns Response.success(null)

        val result = globalMarketRepository.getGlobalMarket(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When global market coin stats required data is missing should return error`() = runTest {
        coEvery { coinNetworkDataSource.getGlobalStats(Currency.USD) } returns Response.success(
            globalStatsApiModel()
        )
        coEvery {
            coinNetworkDataSource.getGlobalMarketCoinStats(Currency.USD)
        } returns Response.success(GlobalMarketCoinStatsApiModel(data = null))

        val result = globalMarketRepository.getGlobalMarket(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When global stats request throws exception should return error`() = runTest {
        coEvery { coinNetworkDataSource.getGlobalStats(Currency.USD) } throws IOException()

        val result = globalMarketRepository.getGlobalMarket(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When global market coin stats request throws exception should return error`() = runTest {
        coEvery { coinNetworkDataSource.getGlobalStats(Currency.USD) } returns Response.success(
            globalStatsApiModel()
        )
        coEvery { coinNetworkDataSource.getGlobalMarketCoinStats(Currency.USD) } throws
            IOException()

        val result = globalMarketRepository.getGlobalMarket(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When global market request is cancelled should rethrow cancellation`() = runTest {
        val cancellationException = CancellationException("Cancelled")

        coEvery { coinNetworkDataSource.getGlobalStats(Currency.USD) } throws cancellationException

        try {
            globalMarketRepository.getGlobalMarket(currency = Currency.USD)
            throw AssertionError("Expected CancellationException")
        } catch (e: CancellationException) {
            assertThat(e).isSameInstanceAs(cancellationException)
        }
    }

    private fun globalStatsApiModel() = GlobalStatsApiModel(
        data = GlobalStatsData(
            totalMarketCap = "2410000000000",
            total24hVolume = "98200000000",
            btcDominance = 54.2
        )
    )

    private fun globalMarketCoinStatsApiModel() = GlobalMarketCoinStatsApiModel(
        data = GlobalMarketCoinStatsDataHolder(
            stats = GlobalMarketCoinStatsData(
                positiveChanges = 2841,
                negativeChanges = 1893
            )
        )
    )

    private companion object {
        const val ERROR_MESSAGE = "Unable to fetch global market"
    }
}

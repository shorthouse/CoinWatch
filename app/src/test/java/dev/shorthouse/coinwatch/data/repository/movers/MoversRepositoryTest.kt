package dev.shorthouse.coinwatch.data.repository.movers

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.MoversMapper
import dev.shorthouse.coinwatch.data.source.local.datastore.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.MoverCoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MoversApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.MoversData
import dev.shorthouse.coinwatch.rule.MainDispatcherRule
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

class MoversRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var moversRepository: MoversRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        moversRepository = MoversRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            moversMapper = MoversMapper()
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When movers data is valid should return success`() = runTest {
        coEvery {
            coinNetworkDataSource.getMovers(CoinSort.Gainers, Currency.EUR)
        } returns Response.success(moversApiModel("Qwsogvtv82FCd", "14.27"))
        coEvery {
            coinNetworkDataSource.getMovers(CoinSort.Losers, Currency.EUR)
        } returns Response.success(moversApiModel("a91GCGd_U96cF", "-11.62"))

        val result = moversRepository.getMovers(currency = Currency.EUR)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        val movers = (result as Result.Success).data
        assertThat(movers.topGainer.id).isEqualTo("Qwsogvtv82FCd")
        assertThat(movers.topLoser.id).isEqualTo("a91GCGd_U96cF")
    }

    @Test
    fun `When gainers response is unsuccessful should return error`() = runTest {
        coEvery {
            coinNetworkDataSource.getMovers(CoinSort.Gainers, Currency.USD)
        } returns Response.error(404, "Movers unavailable".toResponseBody())
        coEvery {
            coinNetworkDataSource.getMovers(CoinSort.Losers, Currency.USD)
        } returns Response.success(moversApiModel("l1", "-11.62"))

        val result = moversRepository.getMovers(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When response body is null should return error`() = runTest {
        coEvery {
            coinNetworkDataSource.getMovers(CoinSort.Gainers, Currency.USD)
        } returns Response.success(null)
        coEvery {
            coinNetworkDataSource.getMovers(CoinSort.Losers, Currency.USD)
        } returns Response.success(moversApiModel("l1", "-11.62"))

        val result = moversRepository.getMovers(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When mapped movers are empty should return error`() = runTest {
        coEvery {
            coinNetworkDataSource.getMovers(CoinSort.Gainers, Currency.USD)
        } returns Response.success(MoversApiModel(moversData = MoversData(coins = emptyList())))
        coEvery {
            coinNetworkDataSource.getMovers(CoinSort.Losers, Currency.USD)
        } returns Response.success(moversApiModel("l1", "-11.62"))

        val result = moversRepository.getMovers(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When request throws exception should return error`() = runTest {
        coEvery {
            coinNetworkDataSource.getMovers(CoinSort.Gainers, Currency.USD)
        } throws IOException()

        val result = moversRepository.getMovers(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    private fun moversApiModel(id: String, change: String) = MoversApiModel(
        moversData = MoversData(
            coins = listOf(
                MoverCoinApiModel(
                    id = id,
                    symbol = "SYM",
                    name = "Name",
                    imageUrl = "https://cdn.coinranking.com/icon.svg",
                    currentPrice = "29446.336548759988",
                    priceChangePercentage24h = change,
                    sparkline = listOf("29100", "29250", "29446")
                )
            )
        )
    )

    private companion object {
        const val ERROR_MESSAGE = "Unable to fetch movers"
    }
}

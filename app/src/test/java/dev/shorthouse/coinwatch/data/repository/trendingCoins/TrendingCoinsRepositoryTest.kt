package dev.shorthouse.coinwatch.data.repository.trendingCoins

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.mapper.TrendingCoinMapper
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.CoinNetworkDataSource
import dev.shorthouse.coinwatch.data.source.remote.model.TrendingCoinApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.TrendingCoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.TrendingCoinsData
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.TrendingCoin
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

class TrendingCoinsRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var trendingCoinsRepository: TrendingCoinsRepository

    @MockK
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        trendingCoinsRepository = TrendingCoinsRepositoryImpl(
            coinNetworkDataSource = coinNetworkDataSource,
            trendingCoinMapper = TrendingCoinMapper()
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When trending coins data is valid should return success`() = runTest {
        coEvery {
            coinNetworkDataSource.getTrendingCoins(Currency.EUR)
        } returns Response.success(trendingCoinsApiModel())

        val result = trendingCoinsRepository.getTrendingCoins(currency = Currency.EUR)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).containsExactly(
            TrendingCoin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29446.336548759988", currency = Currency.EUR),
                priceChangePercentage24h = Percentage("1.76833"),
                sparkline = persistentListOf(
                    BigDecimal("29100"),
                    BigDecimal("29250"),
                    BigDecimal("29446")
                )
            )
        )
    }

    @Test
    fun `When response is unsuccessful should return error`() = runTest {
        coEvery { coinNetworkDataSource.getTrendingCoins(Currency.USD) } returns Response.error(
            404,
            "Trending coins unavailable".toResponseBody()
        )

        val result = trendingCoinsRepository.getTrendingCoins(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When response body is null should return error`() = runTest {
        coEvery {
            coinNetworkDataSource.getTrendingCoins(Currency.USD)
        } returns Response.success(null)

        val result = trendingCoinsRepository.getTrendingCoins(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When mapped coins are empty should return error`() = runTest {
        coEvery { coinNetworkDataSource.getTrendingCoins(Currency.USD) } returns Response.success(
            TrendingCoinsApiModel(trendingCoinsData = TrendingCoinsData(coins = emptyList()))
        )

        val result = trendingCoinsRepository.getTrendingCoins(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun `When request throws exception should return error`() = runTest {
        coEvery { coinNetworkDataSource.getTrendingCoins(Currency.USD) } throws IOException()

        val result = trendingCoinsRepository.getTrendingCoins(currency = Currency.USD)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(ERROR_MESSAGE)
    }

    private fun trendingCoinsApiModel() = TrendingCoinsApiModel(
        trendingCoinsData = TrendingCoinsData(
            coins = listOf(
                TrendingCoinApiModel(
                    id = "Qwsogvtv82FCd",
                    symbol = "BTC",
                    name = "Bitcoin",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = "29446.336548759988",
                    priceChangePercentage24h = "1.76833",
                    rank = 1,
                    sparkline = listOf("29100", "29250", "29446")
                )
            )
        )
    )

    private companion object {
        const val ERROR_MESSAGE = "Unable to fetch trending coins"
    }
}

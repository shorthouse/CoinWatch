package dev.shorthouse.coinwatch.data.source.remote

import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.data.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.preferences.global.Currency
import dev.shorthouse.coinwatch.data.source.remote.model.CoinsApiModel
import dev.shorthouse.coinwatch.data.source.remote.model.FavouriteCoinsApiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class CoinNetworkDataSourceTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var coinNetworkDataSource: CoinNetworkDataSource

    @MockK
    private lateinit var coinApi: CoinApi

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        coinNetworkDataSource = CoinNetworkDataSourceImpl(
            coinApi = coinApi
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When get coins should call API with expected queries`() = runTest {
        // Arrange
        val coinSort = CoinSort.Gainers
        val currency = Currency.EUR

        coEvery { coinApi.getCoins(any(), any(), any()) } returns
            Response.success(CoinsApiModel(null))

        // Act
        coinNetworkDataSource.getCoins(
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        coVerify(exactly = 1) {
            coinApi.getCoins(
                orderBy = "change",
                orderDirection = "desc",
                currencyUUID = "5k-_VTxqtCEI"
            )
        }
    }

    @Test
    fun `When get favourite coins should call API with expected queries`() = runTest {
        // Arrange
        val coinIds = listOf("Qwsogvtv82FCd")
        val coinSort = CoinSort.MarketCap
        val currency = Currency.USD

        coEvery { coinApi.getFavouriteCoins(any(), any(), any()) } returns
            Response.success(FavouriteCoinsApiModel(null))

        // Act
        coinNetworkDataSource.getFavouriteCoins(
            coinIds = coinIds,
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        coVerify(exactly = 1) {
            coinApi.getFavouriteCoins(
                coinIds = coinIds,
                orderBy = "marketCap",
                orderDirection = "desc",
                currencyUUID = "yhjMzLPhuIDl"
            )
        }
    }
}

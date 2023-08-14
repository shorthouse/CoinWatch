package dev.shorthouse.coinwatch.ui.screen.detail

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Constants
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.domain.DeleteFavouriteCoinUseCase
import dev.shorthouse.coinwatch.domain.GetCoinChartUseCase
import dev.shorthouse.coinwatch.domain.GetCoinDetailUseCase
import dev.shorthouse.coinwatch.domain.InsertFavouriteCoinUseCase
import dev.shorthouse.coinwatch.domain.IsCoinFavouriteUseCase
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetail
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockkClass
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoinDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var viewModel: CoinDetailViewModel

    @RelaxedMockK
    private lateinit var getCoinDetailUseCase: GetCoinDetailUseCase

    @RelaxedMockK
    private lateinit var getCoinChartUseCase: GetCoinChartUseCase

    @RelaxedMockK
    private lateinit var isCoinFavouriteUseCase: IsCoinFavouriteUseCase

    @RelaxedMockK
    private lateinit var insertFavouriteCoinUseCase: InsertFavouriteCoinUseCase

    @RelaxedMockK
    private lateinit var deleteFavouriteCoinUseCase: DeleteFavouriteCoinUseCase

    @RelaxedMockK
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { savedStateHandle.get<String>(Constants.PARAM_COIN_ID) } returns "Qwsogvtv82FCd"

        viewModel = CoinDetailViewModel(
            savedStateHandle = savedStateHandle,
            getCoinDetailUseCase = getCoinDetailUseCase,
            getCoinChartUseCase = getCoinChartUseCase,
            isCoinFavouriteUseCase = isCoinFavouriteUseCase,
            insertFavouriteCoinUseCase = insertFavouriteCoinUseCase,
            deleteFavouriteCoinUseCase = deleteFavouriteCoinUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `uiState initialises with loading state`() = runTest {
        // Arrange
        val expectedUiState = CoinDetailUiState.Loading

        // Act

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `uiState error when coin detail returns error result`() = runTest {
        // Arrange
        val coinChart = mockkClass(CoinChart::class)
        val errorMessage = "Coin detail error"
        val expectedUiState = CoinDetailUiState.Error(errorMessage)

        every { getCoinDetailUseCase(any()) } returns flowOf(Result.Error(errorMessage))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(false))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `uiState error when coin chart returns error result`() = runTest {
        // Arrange
        val errorMessage = "Coin chart error"
        val coinDetail = mockkClass(CoinDetail::class)
        val expectedUiState = CoinDetailUiState.Error(errorMessage)

        every { getCoinDetailUseCase(any()) } returns flowOf(Result.Success(coinDetail))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Error(errorMessage))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(false))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `uiState error when coin favourite returns error result`() = runTest {
        // Arrange
        val errorMessage = "Coin favourite error"
        val coinDetail = mockkClass(CoinDetail::class)
        val coinChart = mockkClass(CoinChart::class)
        val expectedUiState = CoinDetailUiState.Error(errorMessage)

        every { getCoinDetailUseCase(any()) } returns flowOf(Result.Success(coinDetail))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Error(errorMessage))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `uiState success when all use cases return success`() = runTest {
        // Arrange
        val coinDetail = mockkClass(CoinDetail::class)
        val coinChart = mockkClass(CoinChart::class)
        val isCoinFavourite = false

        val expectedUiState = CoinDetailUiState.Success(
            coinDetail = coinDetail,
            coinChart = coinChart,
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = isCoinFavourite
        )

        every { getCoinDetailUseCase(any()) } returns flowOf(Result.Success(coinDetail))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(isCoinFavourite))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `uiState chart period updates when calling updateChartPeriod`() = runTest {
        // Arrange
        val coinDetail = mockkClass(CoinDetail::class)
        val coinChart = mockkClass(CoinChart::class)
        val isCoinFavourite = false

        val expectedUiState = CoinDetailUiState.Success(
            coinDetail = coinDetail,
            coinChart = coinChart,
            chartPeriod = ChartPeriod.Week,
            isCoinFavourite = isCoinFavourite
        )

        every { getCoinDetailUseCase(any()) } returns flowOf(Result.Success(coinDetail))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(isCoinFavourite))

        // Act
        viewModel.initialiseUiState()
        viewModel.updateChartPeriod(ChartPeriod.Week)

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `toggleIsCoinFavourite success with unfavourited coin, should favourite coin`() = runTest {
        // Arrange
        val coinId = "Qwsogvtv82FCd"
        every { isCoinFavouriteUseCase(coinId = coinId) } returns flowOf(Result.Success(false))
        coEvery { insertFavouriteCoinUseCase(any()) } just Runs

        // Act
        viewModel.toggleIsCoinFavourite()

        // Assert
        coVerify {
            insertFavouriteCoinUseCase(
                FavouriteCoin(
                    id = coinId
                )
            )
        }
    }

    @Test
    fun `toggleIsCoinFavourite success with favourited coin, should unfavourite coin`() = runTest {
        // Arrange
        val coinId = "Qwsogvtv82FCd"
        every { isCoinFavouriteUseCase(coinId = coinId) } returns flowOf(Result.Success(true))
        coEvery { deleteFavouriteCoinUseCase(any()) } just Runs

        // Act
        viewModel.toggleIsCoinFavourite()

        // Assert
        coVerify {
            deleteFavouriteCoinUseCase(
                FavouriteCoin(
                    id = coinId
                )
            )
        }
    }

    @Test
    fun `toggleIsCoinFavourite failure, should not favourite or unfavourite coin`() = runTest {
        // Arrange
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Error("Error"))
        coEvery { insertFavouriteCoinUseCase(any()) } just Runs
        coEvery { deleteFavouriteCoinUseCase(any()) } just Runs

        // Act
        viewModel.toggleIsCoinFavourite()

        // Assert
        coVerify {
            isCoinFavouriteUseCase(any())
        }

        coVerify(exactly = 0) {
            insertFavouriteCoinUseCase(any())
            deleteFavouriteCoinUseCase(any())
        }
    }
}

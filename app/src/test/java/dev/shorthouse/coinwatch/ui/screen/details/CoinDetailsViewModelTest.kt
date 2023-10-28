package dev.shorthouse.coinwatch.ui.screen.details

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Constants
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.domain.DeleteFavouriteCoinUseCase
import dev.shorthouse.coinwatch.domain.GetCoinChartUseCase
import dev.shorthouse.coinwatch.domain.GetCoinDetailsUseCase
import dev.shorthouse.coinwatch.domain.InsertFavouriteCoinUseCase
import dev.shorthouse.coinwatch.domain.IsCoinFavouriteUseCase
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetails
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

class CoinDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var viewModel: DetailsViewModel

    @RelaxedMockK
    private lateinit var getCoinDetailsUseCase: GetCoinDetailsUseCase

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

        viewModel = DetailsViewModel(
            savedStateHandle = savedStateHandle,
            getCoinDetailsUseCase = getCoinDetailsUseCase,
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
    fun `When ViewModel is initialised should have loading UI state`() = runTest {
        // Arrange
        val expectedUiState = DetailsUiState.Loading

        // Act

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When coin details returns error should have error UI state`() = runTest {
        // Arrange
        val coinChart = mockkClass(CoinChart::class)
        val errorMessage = "Coin details error"
        val expectedUiState = DetailsUiState.Error(errorMessage)

        every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Error(errorMessage))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(false))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When coin chart returns error should have error UI state`() = runTest {
        // Arrange
        val errorMessage = "Coin chart error"
        val coinDetails = mockkClass(CoinDetails::class)
        val expectedUiState = DetailsUiState.Error(errorMessage)

        every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Success(coinDetails))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Error(errorMessage))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(false))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When is coin favourite returns error should have error UI state`() = runTest {
        // Arrange
        val errorMessage = "Coin favourite error"
        val coinDetails = mockkClass(CoinDetails::class)
        val coinChart = mockkClass(CoinChart::class)
        val expectedUiState = DetailsUiState.Error(errorMessage)

        every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Success(coinDetails))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Error(errorMessage))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When all use cases return success should have success UI state`() = runTest {
        // Arrange
        val coinDetails = mockkClass(CoinDetails::class)
        val coinChart = mockkClass(CoinChart::class)
        val isCoinFavourite = false

        val expectedUiState = DetailsUiState.Success(
            coinDetails = coinDetails,
            coinChart = coinChart,
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = isCoinFavourite
        )

        every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Success(coinDetails))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(isCoinFavourite))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When updating chart period UI state should update with new chart period value`() =
        runTest {
            // Arrange
            val coinDetails = mockkClass(CoinDetails::class)
            val coinChart = mockkClass(CoinChart::class)
            val isCoinFavourite = false

            val expectedUiState = DetailsUiState.Success(
                coinDetails = coinDetails,
                coinChart = coinChart,
                chartPeriod = ChartPeriod.Week,
                isCoinFavourite = isCoinFavourite
            )

            every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Success(coinDetails))
            every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
            every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(isCoinFavourite))

            // Act
            viewModel.initialiseUiState()
            viewModel.updateChartPeriod(ChartPeriod.Week)

            // Assert
            assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
        }

    @Test
    fun `When toggle coin favourite returns success with un-favourited coin should favourite coin`() =
        runTest {
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
    fun `When toggle coin favourite returns success with favourited coin should un-favourite coin`() =
        runTest {
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
    fun `When toggle coin favourite returns error should not attempt to favourite or un-favourite coin`() =
        runTest {
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

package dev.shorthouse.coinwatch.ui.screen.details

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Constants
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.domain.GetCoinChartUseCase
import dev.shorthouse.coinwatch.domain.GetCoinDetailsUseCase
import dev.shorthouse.coinwatch.domain.IsCoinFavouriteUseCase
import dev.shorthouse.coinwatch.domain.ToggleIsCoinFavouriteUseCase
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockkClass
import io.mockk.runs
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTest {

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
    private lateinit var toggleIsCoinFavouriteUseCase: ToggleIsCoinFavouriteUseCase

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
            toggleIsCoinFavouriteUseCase = toggleIsCoinFavouriteUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When ViewModel is initialised should have loading UI state`() {
        // Arrange
        val expectedUiState = DetailsUiState.Loading

        // Act

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When coin details returns error should have error UI state`() {
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
    fun `When coin chart returns error should have error UI state`() {
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
    fun `When is coin favourite returns error should have error UI state`() {
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
    fun `When all use cases return success should have success UI state`() {
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
    fun `When updating chart period UI state should update with new chart period value`() {
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
    fun `When toggling coin favourite should call expected use case`() {
        // Arrange
        val coinId = "Qwsogvtv82FCd"
        val favouriteCoin = FavouriteCoin(id = coinId)
        coEvery { toggleIsCoinFavouriteUseCase(favouriteCoin) } just runs

        // Act
        viewModel.toggleIsCoinFavourite()

        // Assert
        coVerifySequence { toggleIsCoinFavouriteUseCase(favouriteCoin) }
    }
}

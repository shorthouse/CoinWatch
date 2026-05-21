package dev.shorthouse.coinwatch.ui.screen.details

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Constants
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.database.model.FavouriteCoinId
import dev.shorthouse.coinwatch.domain.details.GetCoinChartUseCase
import dev.shorthouse.coinwatch.domain.details.GetCoinDetailsUseCase
import dev.shorthouse.coinwatch.domain.favourites.IsCoinFavouriteUseCase
import dev.shorthouse.coinwatch.domain.favourites.ToggleIsCoinFavouriteUseCase
import dev.shorthouse.coinwatch.domain.reviewprompt.RecordFavouriteAddedUseCase
import dev.shorthouse.coinwatch.domain.reviewprompt.RecordSuccessfulDetailsViewUseCase
import dev.shorthouse.coinwatch.model.CoinChart
import dev.shorthouse.coinwatch.model.CoinDetails
import dev.shorthouse.coinwatch.ui.model.ChartPeriod
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkClass
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
    private lateinit var recordSuccessfulDetailsViewUseCase: RecordSuccessfulDetailsViewUseCase

    @RelaxedMockK
    private lateinit var recordFavouriteAddedUseCase: RecordFavouriteAddedUseCase

    @RelaxedMockK
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { savedStateHandle.get<String>(Constants.PARAM_COIN_ID) } returns "Qwsogvtv82FCd"
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
        createViewModel()

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
        createViewModel()

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
        createViewModel()

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
        createViewModel()

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
        createViewModel()

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
        createViewModel()
        viewModel.updateChartPeriod(ChartPeriod.Week)

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When toggling coin favourite should call expected use case`() {
        // Arrange
        val coinId = "Qwsogvtv82FCd"
        val favouriteCoinId = FavouriteCoinId(id = coinId)
        coEvery { toggleIsCoinFavouriteUseCase(favouriteCoinId = favouriteCoinId) } returns true

        // Act
        createViewModel()
        viewModel.toggleIsCoinFavourite()

        // Assert
        coVerifySequence { toggleIsCoinFavouriteUseCase(favouriteCoinId = favouriteCoinId) }
    }

    @Test
    fun `When uiState reaches Success should record successful details view once`() {
        // Arrange
        val coinDetails = mockkClass(CoinDetails::class)
        val coinChart = mockkClass(CoinChart::class)

        every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Success(coinDetails))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(false))

        // Act
        createViewModel()

        // Assert
        coVerify(exactly = 1) { recordSuccessfulDetailsViewUseCase() }
    }

    @Test
    fun `When chart period changes after Success should not re-record successful details view`() {
        // Arrange
        val coinDetails = mockkClass(CoinDetails::class)
        val coinChart = mockkClass(CoinChart::class)

        every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Success(coinDetails))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(false))

        // Act
        createViewModel()
        viewModel.updateChartPeriod(ChartPeriod.Week)
        viewModel.updateChartPeriod(ChartPeriod.Month)

        // Assert
        coVerify(exactly = 1) { recordSuccessfulDetailsViewUseCase() }
    }

    @Test
    fun `When favourite state emits multiple values should record successful details view once`() {
        // Arrange
        val coinDetails = mockkClass(CoinDetails::class)
        val coinChart = mockkClass(CoinChart::class)

        every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Success(coinDetails))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(
            Result.Success(false),
            Result.Success(true),
            Result.Success(false),
        )

        // Act
        createViewModel()

        // Assert
        coVerify(exactly = 1) { recordSuccessfulDetailsViewUseCase() }
    }

    @Test
    fun `When uiState reaches Error should not record successful details view`() {
        // Arrange
        val coinChart = mockkClass(CoinChart::class)
        val errorMessage = "Coin details error"

        every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Error(errorMessage))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(false))

        // Act
        createViewModel()

        // Assert
        coVerify(exactly = 0) { recordSuccessfulDetailsViewUseCase() }
    }

    @Test
    fun `When recording successful details view fails should keep success UI state`() {
        // Arrange
        val coinDetails = mockkClass(CoinDetails::class)
        val coinChart = mockkClass(CoinChart::class)

        val expectedUiState = DetailsUiState.Success(
            coinDetails = coinDetails,
            coinChart = coinChart,
            chartPeriod = ChartPeriod.Day,
            isCoinFavourite = false
        )

        every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Success(coinDetails))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(false))
        coEvery { recordSuccessfulDetailsViewUseCase() } throws RuntimeException("Analytics error")

        // Act
        createViewModel()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When recording successful details view fails should retry on next success emission`() {
        // Arrange
        val coinDetails = mockkClass(CoinDetails::class)
        val coinChart = mockkClass(CoinChart::class)

        every { getCoinDetailsUseCase(any()) } returns flowOf(Result.Success(coinDetails))
        every { getCoinChartUseCase(any(), any()) } returns flowOf(Result.Success(coinChart))
        every { isCoinFavouriteUseCase(any()) } returns flowOf(Result.Success(false))
        coEvery { recordSuccessfulDetailsViewUseCase() } throws RuntimeException("Analytics error")

        // Act
        createViewModel()
        viewModel.updateChartPeriod(ChartPeriod.Week)

        // Assert
        coVerify(exactly = 2) { recordSuccessfulDetailsViewUseCase() }
    }

    @Test
    fun `When toggling favourite returns true should record favourite added`() {
        // Arrange
        val favouriteCoinId = FavouriteCoinId(id = "Qwsogvtv82FCd")
        coEvery { toggleIsCoinFavouriteUseCase(favouriteCoinId = favouriteCoinId) } returns true

        // Act
        createViewModel()
        viewModel.toggleIsCoinFavourite()

        // Assert
        coVerify(exactly = 1) { recordFavouriteAddedUseCase() }
    }

    @Test
    fun `When toggling favourite returns false should not record favourite added`() {
        // Arrange
        val favouriteCoinId = FavouriteCoinId(id = "Qwsogvtv82FCd")
        coEvery { toggleIsCoinFavouriteUseCase(favouriteCoinId = favouriteCoinId) } returns false

        // Act
        createViewModel()
        viewModel.toggleIsCoinFavourite()

        // Assert
        coVerify(exactly = 0) { recordFavouriteAddedUseCase() }
    }

    private fun createViewModel() {
        viewModel = DetailsViewModel(
            savedStateHandle = savedStateHandle,
            getCoinDetailsUseCase = getCoinDetailsUseCase,
            getCoinChartUseCase = getCoinChartUseCase,
            isCoinFavouriteUseCase = isCoinFavouriteUseCase,
            toggleIsCoinFavouriteUseCase = toggleIsCoinFavouriteUseCase,
            recordSuccessfulDetailsViewUseCase = recordSuccessfulDetailsViewUseCase,
            recordFavouriteAddedUseCase = recordFavouriteAddedUseCase
        )
    }
}

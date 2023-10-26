package dev.shorthouse.coinwatch.ui.screen.list

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.domain.GetCoinsUseCase
import dev.shorthouse.coinwatch.domain.GetFavouriteCoinsUseCase
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.ui.model.TimeOfDay
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import java.time.LocalDateTime
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CoinListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var viewModel: ListViewModel

    @RelaxedMockK
    private lateinit var getCoinsUseCase: GetCoinsUseCase

    @RelaxedMockK
    private lateinit var getFavouriteCoinsUseCase: GetFavouriteCoinsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = ListViewModel(
            getCoinsUseCase = getCoinsUseCase,
            getFavouriteCoinsUseCase = getFavouriteCoinsUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When ViewModel is initialised should have loading UI state`() = runTest {
        // Arrange
        val expectedUiState = ListUiState.Loading

        // Act

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When coins returns error should have error UI state`() = runTest {
        // Arrange
        val errorMessage = "Coins error"
        val expectedUiState = ListUiState.Error(errorMessage)

        every { getCoinsUseCase() } returns flowOf(Result.Error(errorMessage))
        every { getFavouriteCoinsUseCase() } returns flowOf(Result.Success(emptyList()))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When favourite coins returns error should have error UI state`() = runTest {
        // Arrange
        val errorMessage = "Favourite coins error"
        val expectedUiState = ListUiState.Error(errorMessage)

        every { getCoinsUseCase() } returns flowOf(Result.Success(emptyList()))
        every { getFavouriteCoinsUseCase() } returns flowOf(Result.Error(errorMessage))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When favourite coins and coins return success should have success UI state`() = runTest {
        // Arrange
        val coins = persistentListOf(
            Coin(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "",
                currentPrice = Price("200.0"),
                priceChangePercentage24h = Percentage("1.0"),
                prices24h = persistentListOf()
            ),
            Coin(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "",
                currentPrice = Price("100.0"),
                priceChangePercentage24h = Percentage("2.0"),
                prices24h = persistentListOf()
            )
        )

        val returnedFavouriteCoins = persistentListOf(
            Coin(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "",
                currentPrice = Price("100.0"),
                priceChangePercentage24h = Percentage("2.0"),
                prices24h = persistentListOf()
            )
        )

        val expectedFavouriteCoins = persistentListOf(
            Coin(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                imageUrl = "",
                currentPrice = Price("100.0"),
                priceChangePercentage24h = Percentage("2.0"),
                prices24h = persistentListOf()
            )
        )

        val expectedTimeOfDay = when (LocalDateTime.now().hour) {
            in 0..11 -> TimeOfDay.Morning
            in 12..17 -> TimeOfDay.Afternoon
            else -> TimeOfDay.Evening
        }

        val expectedUiState = ListUiState.Success(
            coins = coins,
            favouriteCoins = expectedFavouriteCoins,
            timeOfDay = expectedTimeOfDay
        )

        every { getCoinsUseCase() } returns flowOf(Result.Success(coins))
        every { getFavouriteCoinsUseCase() } returns flowOf(Result.Success(returnedFavouriteCoins))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When calculating time of day with morning hour, should return morning enum value`() =
        runTest {
            // Arrange
            val lowerBoundHour = 0
            val upperBoundHour = 11
            val expectedTimeOfDay = TimeOfDay.Morning

            // Act
            val lowerBoundHourTimeOfDay = viewModel.calculateTimeOfDay(lowerBoundHour)
            val upperBoundHourTimeOfDay = viewModel.calculateTimeOfDay(upperBoundHour)

            // Assert
            assertThat(lowerBoundHourTimeOfDay).isEqualTo(expectedTimeOfDay)
            assertThat(upperBoundHourTimeOfDay).isEqualTo(expectedTimeOfDay)
        }

    @Test
    fun `When calculating time of day with afternoon hour, should return afternoon enum value`() =
        runTest {
            // Arrange
            val lowerBoundHour = 12
            val upperBoundHour = 17
            val expectedTimeOfDay = TimeOfDay.Afternoon

            // Act
            val lowerBoundHourTimeOfDay = viewModel.calculateTimeOfDay(lowerBoundHour)
            val upperBoundHourTimeOfDay = viewModel.calculateTimeOfDay(upperBoundHour)

            // Assert
            assertThat(lowerBoundHourTimeOfDay).isEqualTo(expectedTimeOfDay)
            assertThat(upperBoundHourTimeOfDay).isEqualTo(expectedTimeOfDay)
        }

    @Test
    fun `When calculating time of day with evening hour, should return evening enum value`() =
        runTest {
            // Arrange
            val lowerBoundHour = 18
            val upperBoundHour = 23
            val expectedTimeOfDay = TimeOfDay.Evening

            // Act
            val lowerBoundHourTimeOfDay = viewModel.calculateTimeOfDay(lowerBoundHour)
            val upperBoundHourTimeOfDay = viewModel.calculateTimeOfDay(upperBoundHour)

            // Assert
            assertThat(lowerBoundHourTimeOfDay).isEqualTo(expectedTimeOfDay)
            assertThat(upperBoundHourTimeOfDay).isEqualTo(expectedTimeOfDay)
        }
}

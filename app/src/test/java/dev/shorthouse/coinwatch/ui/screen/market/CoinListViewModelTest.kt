package dev.shorthouse.coinwatch.ui.screen.market

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.MainDispatcherRule
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.domain.GetCoinsUseCase
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
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
    private lateinit var viewModel: MarketViewModel

    @RelaxedMockK
    private lateinit var getCoinsUseCase: GetCoinsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = MarketViewModel(
            getCoinsUseCase = getCoinsUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `When ViewModel is initialised should have loading UI state`() = runTest {
        // Arrange
        val expectedUiState = MarketUiState.Loading

        // Act

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When coins returns error should have error UI state`() = runTest {
        // Arrange
        val errorMessage = "Coins error"
        val expectedUiState = MarketUiState.Error(errorMessage)

        every { getCoinsUseCase() } returns flowOf(Result.Error(errorMessage))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }

    @Test
    fun `When coins return success should have success UI state`() = runTest {
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

        val expectedUiState = MarketUiState.Success(
            coins = coins
        )

        every { getCoinsUseCase() } returns flowOf(Result.Success(coins))

        // Act
        viewModel.initialiseUiState()

        // Assert
        assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
    }
}

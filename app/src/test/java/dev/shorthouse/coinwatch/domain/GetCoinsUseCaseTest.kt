package dev.shorthouse.coinwatch.domain

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.datastore.UserPreferences
import dev.shorthouse.coinwatch.data.datastore.UserPreferencesRepository
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.model.Coin
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCoinsUseCaseTest {

    // Class under test
    private lateinit var getCoinsUseCase: GetCoinsUseCase

    @MockK
    private lateinit var coinRepository: CoinRepository

    @MockK
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getCoinsUseCase = GetCoinsUseCase(
            coinRepository = coinRepository,
            userPreferencesRepository = userPreferencesRepository
        )
    }

    @Test
    fun `When coins returns success with list should return coin list`() = runTest {
        // Arrange
        val expectedResult = Result.Success(
            listOf(
                Coin(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29490.954785191607"),
                    priceChangePercentage24h = Percentage("-0.96"),
                    prices24h = persistentListOf(
                        BigDecimal("29790.15810429195"),
                        BigDecimal("29782.07714670252"),
                        BigDecimal("29436.47984833588"),
                        BigDecimal("29510.92753539824"),
                        BigDecimal("29482.564008512305")
                    )
                )
            )
        )

        every {
            userPreferencesRepository.userPreferencesFlow
        } returns flowOf(
            UserPreferences()
        )

        every {
            coinRepository.getCoins(
                coinSort = any(),
                currency = any()
            )
        } returns flowOf(
            Result.Success(
                listOf(
                    Coin(
                        id = "Qwsogvtv82FCd",
                        name = "Bitcoin",
                        symbol = "BTC",
                        imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                        currentPrice = Price("29490.954785191607"),
                        priceChangePercentage24h = Percentage("-0.96"),
                        prices24h = persistentListOf(
                            BigDecimal("29790.15810429195"),
                            BigDecimal("29782.07714670252"),
                            BigDecimal("29436.47984833588"),
                            BigDecimal("29510.92753539824"),
                            BigDecimal("29482.564008512305")
                        )
                    )
                )
            )
        )

        // Act
        val getCoinsResult = getCoinsUseCase().first()

        assertThat(getCoinsResult).isInstanceOf(Result.Success::class.java)
        assertThat((getCoinsResult as Result.Success).data)
            .isEqualTo(expectedResult.data)
    }

    @Test
    fun `When get coins returns error should return error`() = runTest {
        // Arrange
        val errorMessage = "Coins error"
        val expectedResult = Result.Error<List<Coin>>(errorMessage)

        every {
            userPreferencesRepository.userPreferencesFlow
        } returns flowOf(
            UserPreferences()
        )

        every {
            coinRepository.getCoins(
                coinSort = any(),
                currency = any()
            )
        } returns flowOf(
            Result.Error(errorMessage)
        )

        // Act
        val getCoinsResult = getCoinsUseCase().first()

        // Assert
        assertThat(getCoinsResult).isInstanceOf(Result.Error::class.java)
        assertThat((getCoinsResult as Result.Error).message)
            .isEqualTo(expectedResult.message)
    }

    @Test
    fun `When get coins returns success with empty list should return empty list`() = runTest {
        // Arrange
        val expectedResult = Result.Success<List<Coin>>(emptyList())

        every {
            userPreferencesRepository.userPreferencesFlow
        } returns flowOf(
            UserPreferences()
        )

        every {
            coinRepository.getCoins(
                coinSort = any(),
                currency = any()
            )
        } returns flowOf(
            Result.Success(emptyList())
        )

        // Act
        val getCoinsResult = getCoinsUseCase().first()

        // Assert
        assertThat(getCoinsResult).isInstanceOf(Result.Success::class.java)
        assertThat((getCoinsResult as Result.Success).data)
            .isEqualTo(expectedResult.data)
    }
}

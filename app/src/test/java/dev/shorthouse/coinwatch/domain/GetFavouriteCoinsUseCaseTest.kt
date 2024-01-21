package dev.shorthouse.coinwatch.domain

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.data.repository.favouriteCoinId.FavouriteCoinIdRepository
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.data.userPreferences.UserPreferences
import dev.shorthouse.coinwatch.data.userPreferences.UserPreferencesRepository
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

class GetFavouriteCoinsUseCaseTest {

    // Class under test
    private lateinit var getFavouriteCoinsUseCase: GetFavouriteCoinsUseCase

    @MockK
    private lateinit var favouriteCoinIdRepository: FavouriteCoinIdRepository

    @MockK
    private lateinit var coinRepository: CoinRepository

    @MockK
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getFavouriteCoinsUseCase = GetFavouriteCoinsUseCase(
            favouriteCoinIdRepository = favouriteCoinIdRepository,
            coinRepository = coinRepository,
            userPreferencesRepository = userPreferencesRepository
        )
    }

    @Test
    fun `When favourite coins returns success with list should return coin list`() = runTest {
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
            favouriteCoinIdRepository.getFavouriteCoinIds()
        } returns flowOf(
            Result.Success(
                listOf(
                    FavouriteCoinId(id = "Qwsogvtv82FCd")
                )
            )
        )

        every {
            coinRepository.getCoins(
                coinIds = listOf("Qwsogvtv82FCd"),
                coinSort = CoinSort.MarketCap,
                currency = Currency.USD
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
        val getFavouriteCoinsResult = getFavouriteCoinsUseCase().first()

        // Assert
        assertThat(getFavouriteCoinsResult).isInstanceOf(Result.Success::class.java)
        assertThat((getFavouriteCoinsResult as Result.Success).data)
            .isEqualTo(expectedResult.data)
    }

    @Test
    fun `When favourite coins returns error should return error`() = runTest {
        // Arrange
        val errorMessage = "Unable to fetch favourite coins"
        val expectedResult = Result.Error<List<Coin>>(
            message = errorMessage
        )

        every {
            favouriteCoinIdRepository.getFavouriteCoinIds()
        } returns flowOf(Result.Error(errorMessage))

        // Act
        val getFavouriteCoinsResult = getFavouriteCoinsUseCase().first()

        // Assert
        assertThat(getFavouriteCoinsResult).isInstanceOf(Result.Error::class.java)
        assertThat((getFavouriteCoinsResult as Result.Error).message)
            .isEqualTo(expectedResult.message)
    }

    @Test
    fun `When favourite coins returns success with empty list should return empty list`() =
        runTest {
            // Arrange
            val expectedResult = Result.Success(emptyList<Coin>())

            every {
                favouriteCoinIdRepository.getFavouriteCoinIds()
            } returns flowOf(Result.Success(emptyList()))

            // Act
            val getFavouriteCoinsResult = getFavouriteCoinsUseCase().first()

            // Assert
            assertThat(getFavouriteCoinsResult).isInstanceOf(Result.Success::class.java)
            assertThat((getFavouriteCoinsResult as Result.Success).data)
                .isEqualTo(expectedResult.data)
        }
}

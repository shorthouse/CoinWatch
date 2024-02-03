package dev.shorthouse.coinwatch.domain

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.favouriteCoin.FavouriteCoinRepository
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoin
import dev.shorthouse.coinwatch.data.source.local.model.FavouriteCoinId
import dev.shorthouse.coinwatch.data.userPreferences.CoinSort
import dev.shorthouse.coinwatch.data.userPreferences.Currency
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.just
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateCachedFavouriteCoinsUseCaseTest {

    // Class under test
    private lateinit var updateCachedFavouriteCoinsUseCase: UpdateCachedFavouriteCoinsUseCase

    @MockK
    private lateinit var favouriteCoinRepository: FavouriteCoinRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        updateCachedFavouriteCoinsUseCase = UpdateCachedFavouriteCoinsUseCase(
            favouriteCoinRepository = favouriteCoinRepository,
        )
    }

    @Test
    fun `When remote favourite coins has error result should return error and not update cache`() = runTest {
        // Arrange
        val coinIds = listOf(FavouriteCoinId("Qwsogvtv82FCd"))
        val rawCoinIds = listOf("Qwsogvtv82FCd")
        val currency = Currency.USD
        val coinSort = CoinSort.Volume24h
        val errorMessage = "Unable to fetch network favourite coins list"

        coEvery {
            favouriteCoinRepository.getRemoteFavouriteCoins(
                coinIds = rawCoinIds,
                currency = currency,
                coinSort = coinSort
            )
        } returns Result.Error(errorMessage)

        val expectedResult = Result.Error<List<FavouriteCoin>>(errorMessage)

        // Act
        val result = updateCachedFavouriteCoinsUseCase(
            coinIds = coinIds,
            currency = currency,
            coinSort = coinSort
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(expectedResult.message)

        coVerify(exactly = 1) {
            favouriteCoinRepository.getRemoteFavouriteCoins(
                coinIds = rawCoinIds,
                coinSort = coinSort,
                currency = currency
            )
        }

        confirmVerified(favouriteCoinRepository)
    }

    @Test
    fun `When remote favourite coins has success result should update cache and return success`() = runTest {
        // Arrange
        val coinIds = listOf(FavouriteCoinId("Qwsogvtv82FCd"))
        val rawCoinIds = listOf("Qwsogvtv82FCd")
        val currency = Currency.USD
        val coinSort = CoinSort.Volume24h
        val favouriteCoins = listOf(
            FavouriteCoin(
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

        coEvery {
            favouriteCoinRepository.getRemoteFavouriteCoins(
                coinIds = rawCoinIds,
                currency = currency,
                coinSort = coinSort
            )
        } returns Result.Success(favouriteCoins)

        coEvery {
            favouriteCoinRepository.updateCachedFavouriteCoins(favouriteCoins = favouriteCoins)
        } just Runs

        val expectedResult = Result.Success(favouriteCoins)

        // Act
        val result = updateCachedFavouriteCoinsUseCase(
            coinIds = coinIds,
            currency = currency,
            coinSort = coinSort
        )

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedResult.data)

        coVerify(exactly = 1) {
            favouriteCoinRepository.getRemoteFavouriteCoins(
                coinIds = rawCoinIds,
                coinSort = coinSort,
                currency = currency
            )

            favouriteCoinRepository.updateCachedFavouriteCoins(
                favouriteCoins = favouriteCoins
            )
        }

        confirmVerified(favouriteCoinRepository)
    }
}

package dev.shorthouse.coinwatch.domain

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.source.local.preferences.common.CoinSort
import dev.shorthouse.coinwatch.data.source.local.preferences.global.Currency
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import dev.shorthouse.coinwatch.data.source.local.database.model.Coin
import dev.shorthouse.coinwatch.domain.market.UpdateCachedCoinsUseCase
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateCachedCoinsUseCaseTest {

    // Class under test
    private lateinit var updateCachedCoinsUseCase: UpdateCachedCoinsUseCase

    @MockK
    private lateinit var coinRepository: CoinRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        updateCachedCoinsUseCase = UpdateCachedCoinsUseCase(
            coinRepository = coinRepository
        )
    }

    @Test
    fun `When remote coins success should refresh cached coins and return success`() = runTest {
        // Arrange
        val coinSort = CoinSort.Gainers
        val currency = Currency.EUR

        val remoteCoinsResult = Result.Success(
            listOf(
                Coin(
                    id = "Qwsogvtv82FCd",
                    name = "Bitcoin",
                    symbol = "BTC",
                    imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                    currentPrice = Price("29490.954785191607", currency = currency),
                    priceChangePercentage24h = Percentage("-0.96"),
                )
            )
        )

        coEvery {
            coinRepository.getRemoteCoins(
                coinSort = coinSort,
                currency = currency
            )
        } returns remoteCoinsResult

        coEvery {
            coinRepository.updateCachedCoins(remoteCoinsResult.data)
        } just Runs

        // Act
        val refreshCachedCoinResult = updateCachedCoinsUseCase(
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(refreshCachedCoinResult).isInstanceOf(Result.Success::class.java)
        assertThat((refreshCachedCoinResult as Result.Success).data)
            .isEqualTo(remoteCoinsResult.data)

        coVerifySequence {
            coinRepository.getRemoteCoins(
                coinSort = coinSort,
                currency = currency
            )
            coinRepository.updateCachedCoins(remoteCoinsResult.data)
        }
    }

    @Test
    fun `When remote coins error should not call refresh and should return error`() = runTest {
        // Arrange
        val coinSort = CoinSort.Gainers
        val currency = Currency.EUR

        val remoteCoinsResult = Result.Error<List<Coin>>(
            "Error message"
        )

        coEvery {
            coinRepository.getRemoteCoins(
                coinSort = coinSort,
                currency = currency
            )
        } returns remoteCoinsResult

        // Act
        val refreshCachedCoinResult = updateCachedCoinsUseCase(
            coinSort = coinSort,
            currency = currency
        )

        // Assert
        assertThat(refreshCachedCoinResult).isInstanceOf(Result.Error::class.java)
        assertThat((refreshCachedCoinResult as Result.Error).message)
            .isEqualTo(remoteCoinsResult.message)

        coVerifySequence {
            coinRepository.getRemoteCoins(
                coinSort = coinSort,
                currency = currency
            )
        }
    }
}

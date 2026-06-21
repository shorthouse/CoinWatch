package dev.shorthouse.coinwatch.domain.pulse

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.globalMarket.GlobalMarketRepository
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.GlobalMarket
import dev.shorthouse.coinwatch.model.Price
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import java.math.BigDecimal
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetGlobalMarketUseCaseTest {

    // Class under test
    private lateinit var getGlobalMarketUseCase: GetGlobalMarketUseCase

    @MockK
    private lateinit var globalMarketRepository: GlobalMarketRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getGlobalMarketUseCase = GetGlobalMarketUseCase(
            globalMarketRepository = globalMarketRepository
        )
    }

    @Test
    fun `When repository returns success should return success for selected currency`() = runTest {
        // Arrange
        val globalMarketResult = Result.Success(globalMarket(currency = Currency.GBP))

        coEvery {
            globalMarketRepository.getGlobalMarket(currency = Currency.GBP)
        } returns globalMarketResult

        // Act
        val result = getGlobalMarketUseCase(currency = Currency.GBP)

        // Assert
        assertThat(result).isSameInstanceAs(globalMarketResult)

        coVerifySequence {
            globalMarketRepository.getGlobalMarket(currency = Currency.GBP)
        }
    }

    @Test
    fun `When repository returns error should return error`() = runTest {
        // Arrange
        val globalMarketResult = Result.Error<GlobalMarket>(
            "Unable to fetch global market"
        )

        coEvery {
            globalMarketRepository.getGlobalMarket(currency = Currency.USD)
        } returns globalMarketResult

        // Act
        val result = getGlobalMarketUseCase(currency = Currency.USD)

        // Assert
        assertThat(result).isSameInstanceAs(globalMarketResult)

        coVerifySequence {
            globalMarketRepository.getGlobalMarket(currency = Currency.USD)
        }
    }

    private fun globalMarket(currency: Currency): GlobalMarket {
        return GlobalMarket(
            totalMarketCap = Price("2410000000000", currency = currency),
            volume24h = Price("98200000000", currency = currency),
            btcDominancePercentage = BigDecimal("54.2"),
            coinsUp24h = 2841,
            coinsDown24h = 1893
        )
    }
}

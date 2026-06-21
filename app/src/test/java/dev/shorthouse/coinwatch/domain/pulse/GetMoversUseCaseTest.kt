package dev.shorthouse.coinwatch.domain.pulse

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.movers.MoversRepository
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.MoverCoin
import dev.shorthouse.coinwatch.model.Movers
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetMoversUseCaseTest {

    // Class under test
    private lateinit var getMoversUseCase: GetMoversUseCase

    @MockK
    private lateinit var moversRepository: MoversRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getMoversUseCase = GetMoversUseCase(
            moversRepository = moversRepository
        )
    }

    @Test
    fun `When repository returns success should return success for selected currency`() = runTest {
        val moversResult = Result.Success(movers(currency = Currency.GBP))

        coEvery {
            moversRepository.getMovers(currency = Currency.GBP)
        } returns moversResult

        val result = getMoversUseCase(currency = Currency.GBP)

        assertThat(result).isSameInstanceAs(moversResult)

        coVerifySequence {
            moversRepository.getMovers(currency = Currency.GBP)
        }
    }

    @Test
    fun `When repository returns error should return error`() = runTest {
        val moversResult = Result.Error<Movers>("Unable to fetch movers")

        coEvery {
            moversRepository.getMovers(currency = Currency.USD)
        } returns moversResult

        val result = getMoversUseCase(currency = Currency.USD)

        assertThat(result).isSameInstanceAs(moversResult)

        coVerifySequence {
            moversRepository.getMovers(currency = Currency.USD)
        }
    }

    private fun movers(currency: Currency): Movers {
        return Movers(
            topGainer = moverCoin(id = "gainer", currency = currency),
            topLoser = moverCoin(id = "loser", currency = currency),
            mostMovement = persistentListOf()
        )
    }

    private fun moverCoin(id: String, currency: Currency): MoverCoin {
        return MoverCoin(
            id = id,
            name = "Bitcoin",
            symbol = "BTC",
            imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
            currentPrice = Price("29446.336548759988", currency = currency),
            priceChangePercentage24h = Percentage("1.76833"),
            sparkline = persistentListOf()
        )
    }
}

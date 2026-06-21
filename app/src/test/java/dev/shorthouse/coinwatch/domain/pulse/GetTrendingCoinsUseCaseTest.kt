package dev.shorthouse.coinwatch.domain.pulse

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.trendingCoins.TrendingCoinsRepository
import dev.shorthouse.coinwatch.data.source.local.datastore.global.Currency
import dev.shorthouse.coinwatch.model.Percentage
import dev.shorthouse.coinwatch.model.Price
import dev.shorthouse.coinwatch.model.TrendingCoin
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetTrendingCoinsUseCaseTest {

    // Class under test
    private lateinit var getTrendingCoinsUseCase: GetTrendingCoinsUseCase

    @MockK
    private lateinit var trendingCoinsRepository: TrendingCoinsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getTrendingCoinsUseCase = GetTrendingCoinsUseCase(
            trendingCoinsRepository = trendingCoinsRepository
        )
    }

    @Test
    fun `When repository returns success should return success for selected currency`() = runTest {
        val trendingCoinsResult = Result.Success(trendingCoins(currency = Currency.GBP))

        coEvery {
            trendingCoinsRepository.getTrendingCoins(currency = Currency.GBP)
        } returns trendingCoinsResult

        val result = getTrendingCoinsUseCase(currency = Currency.GBP)

        assertThat(result).isSameInstanceAs(trendingCoinsResult)

        coVerifySequence {
            trendingCoinsRepository.getTrendingCoins(currency = Currency.GBP)
        }
    }

    @Test
    fun `When repository returns error should return error`() = runTest {
        val trendingCoinsResult = Result.Error<List<TrendingCoin>>(
            "Unable to fetch trending coins"
        )

        coEvery {
            trendingCoinsRepository.getTrendingCoins(currency = Currency.USD)
        } returns trendingCoinsResult

        val result = getTrendingCoinsUseCase(currency = Currency.USD)

        assertThat(result).isSameInstanceAs(trendingCoinsResult)

        coVerifySequence {
            trendingCoinsRepository.getTrendingCoins(currency = Currency.USD)
        }
    }

    private fun trendingCoins(currency: Currency): List<TrendingCoin> {
        return listOf(
            TrendingCoin(
                id = "Qwsogvtv82FCd",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg",
                currentPrice = Price("29446.336548759988", currency = currency),
                priceChangePercentage24h = Percentage("1.76833"),
                sparkline = persistentListOf()
            )
        )
    }
}

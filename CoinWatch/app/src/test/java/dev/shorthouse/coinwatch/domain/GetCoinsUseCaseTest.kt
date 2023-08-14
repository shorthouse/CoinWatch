package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class GetCoinsUseCaseTest {

    // Class under test
    private lateinit var getCoinsUseCase: GetCoinsUseCase

    @MockK
    private lateinit var coinRepository: CoinRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getCoinsUseCase = GetCoinsUseCase(
            coinRepository = coinRepository
        )
    }

    @Test
    fun `when getCoinsUseCase invoked then getCoins is called`() {
        // Arrange
        every { coinRepository.getCoins() } returns flowOf(Result.Success(emptyList()))

        // Act
        getCoinsUseCase()

        // Assert
        verifySequence {
            coinRepository.getCoins()
        }
    }
}

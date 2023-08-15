package dev.shorthouse.coinwatch.domain

import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.coin.CoinRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
    fun `When use case invoked should get coins`() = runTest {
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

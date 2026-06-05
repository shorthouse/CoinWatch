package dev.shorthouse.coinwatch.domain.pulse

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.Result
import dev.shorthouse.coinwatch.data.repository.feargreed.FearGreedRepository
import dev.shorthouse.coinwatch.model.FearGreed
import dev.shorthouse.coinwatch.model.FearGreedMoodBand
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import java.math.BigDecimal
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetFearGreedUseCaseTest {

    // Class under test
    private lateinit var getFearGreedUseCase: GetFearGreedUseCase

    @MockK
    private lateinit var fearGreedRepository: FearGreedRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getFearGreedUseCase = GetFearGreedUseCase(
            fearGreedRepository = fearGreedRepository
        )
    }

    @Test
    fun `When repository returns success should return success`() = runTest {
        // Arrange
        val fearGreedResult = Result.Success(
            FearGreed(
                value = 42,
                moodBand = FearGreedMoodBand.Fear,
                history = persistentListOf(
                    BigDecimal("35"),
                    BigDecimal("40"),
                    BigDecimal("42")
                )
            )
        )

        coEvery {
            fearGreedRepository.getFearGreed()
        } returns fearGreedResult

        // Act
        val result = getFearGreedUseCase()

        // Assert
        assertThat(result).isSameInstanceAs(fearGreedResult)

        coVerifySequence {
            fearGreedRepository.getFearGreed()
        }
    }

    @Test
    fun `When repository returns error should return error`() = runTest {
        // Arrange
        val fearGreedResult = Result.Error<FearGreed>(
            "Unable to fetch fear and greed"
        )

        coEvery {
            fearGreedRepository.getFearGreed()
        } returns fearGreedResult

        // Act
        val result = getFearGreedUseCase()

        // Assert
        assertThat(result).isSameInstanceAs(fearGreedResult)

        coVerifySequence {
            fearGreedRepository.getFearGreed()
        }
    }
}

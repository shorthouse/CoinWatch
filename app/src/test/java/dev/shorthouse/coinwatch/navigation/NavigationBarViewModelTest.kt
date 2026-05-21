package dev.shorthouse.coinwatch.navigation

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.rule.MainDispatcherRule
import dev.shorthouse.coinwatch.domain.reviewprompt.IsReviewPromptEligibleUseCase
import dev.shorthouse.coinwatch.domain.reviewprompt.RecordReviewPromptAttemptedUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationBarViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Class under test
    private lateinit var viewModel: NavigationBarViewModel

    @RelaxedMockK
    private lateinit var isReviewPromptEligibleUseCase: IsReviewPromptEligibleUseCase

    @RelaxedMockK
    private lateinit var recordReviewPromptAttemptedUseCase: RecordReviewPromptAttemptedUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = NavigationBarViewModel(
            isReviewPromptEligibleUseCase = isReviewPromptEligibleUseCase,
            recordReviewPromptAttemptedUseCase = recordReviewPromptAttemptedUseCase,
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `isReviewPromptEligible returns true when use case returns true`() = runTest {
        // Arrange
        coEvery { isReviewPromptEligibleUseCase() } returns true

        // Act
        val result = viewModel.isReviewPromptEligible()

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `isReviewPromptEligible returns false when use case returns false`() = runTest {
        // Arrange
        coEvery { isReviewPromptEligibleUseCase() } returns false

        // Act
        val result = viewModel.isReviewPromptEligible()

        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `recordReviewPromptAttempted invokes use case`() = runTest {
        // Act
        viewModel.recordReviewPromptAttempted()

        // Assert
        coVerify(exactly = 1) { recordReviewPromptAttemptedUseCase() }
    }
}

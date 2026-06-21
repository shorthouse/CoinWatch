package dev.shorthouse.coinwatch.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.data.repository.reviewPrompt.ReviewPromptRepository
import dev.shorthouse.coinwatch.domain.reviewprompt.IsReviewPromptEligibleUseCase
import dev.shorthouse.coinwatch.domain.reviewprompt.RecordReviewPromptAttemptedUseCase
import java.util.Collections
import java.util.concurrent.atomic.AtomicInteger
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationBarScaffoldTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun when_composedAndResumed_should_invokeIsReviewPromptEligibleOnViewModel() {
        val fakeRepository = FakeReviewPromptRepository(eligible = false)
        val viewModel = createViewModel(fakeRepository)

        composeTestRule.setContent {
            ReviewPromptOnResumeEffect(viewModel = viewModel)
        }

        composeTestRule.waitUntil(timeoutMillis = 2_000) {
            fakeRepository.isReviewPromptEligibleCallCount >= 1
        }

        assertThat(fakeRepository.isReviewPromptEligibleCallCount).isAtLeast(1)
        assertThat(fakeRepository.recordReviewPromptAttemptedCallCount).isEqualTo(0)
    }

    @Test
    fun when_eligibilityReturnsFalse_should_notRecordReviewPromptAttempted() {
        val fakeRepository = FakeReviewPromptRepository(eligible = false)
        val viewModel = createViewModel(fakeRepository)

        composeTestRule.setContent {
            ReviewPromptOnResumeEffect(viewModel = viewModel)
        }

        composeTestRule.waitUntil(timeoutMillis = 2_000) {
            fakeRepository.isReviewPromptEligibleCallCount >= 1
        }

        assertThat(fakeRepository.recordReviewPromptAttemptedCallCount).isEqualTo(0)
    }

    @Test
    fun when_eligibilityReturnsTrue_should_recordAttemptBeforeLaunchingReview() {
        val fakeRepository = FakeReviewPromptRepository(eligible = true)
        val viewModel = createViewModel(fakeRepository)

        composeTestRule.setContent {
            ReviewPromptOnResumeEffect(
                viewModel = viewModel,
                launchReview = {
                    fakeRepository.events += "launchReview"
                }
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 2_000) {
            fakeRepository.events.contains("launchReview")
        }

        assertThat(fakeRepository.events.toList()).containsExactly(
            "isReviewPromptEligible",
            "recordReviewPromptAttempted",
            "launchReview",
        ).inOrder()
    }

    @Test
    fun when_eligibilityReturnsFalse_should_notLaunchReview() {
        val fakeRepository = FakeReviewPromptRepository(eligible = false)
        val viewModel = createViewModel(fakeRepository)
        val launchReviewCallCount = AtomicInteger()

        composeTestRule.setContent {
            ReviewPromptOnResumeEffect(
                viewModel = viewModel,
                launchReview = {
                    launchReviewCallCount.incrementAndGet()
                }
            )
        }

        composeTestRule.waitUntil(timeoutMillis = 2_000) {
            fakeRepository.isReviewPromptEligibleCallCount >= 1
        }

        assertThat(launchReviewCallCount.get()).isEqualTo(0)
    }

    private fun createViewModel(
        fakeRepository: FakeReviewPromptRepository,
    ): NavigationBarViewModel {
        return NavigationBarViewModel(
            isReviewPromptEligibleUseCase = IsReviewPromptEligibleUseCase(fakeRepository),
            recordReviewPromptAttemptedUseCase = RecordReviewPromptAttemptedUseCase(fakeRepository),
        )
    }

    private class FakeReviewPromptRepository(
        private val eligible: Boolean,
    ) : ReviewPromptRepository {
        val events = Collections.synchronizedList(mutableListOf<String>())

        var isReviewPromptEligibleCallCount = 0
            private set
        var recordReviewPromptAttemptedCallCount = 0
            private set

        override suspend fun isReviewPromptEligible(): Boolean {
            isReviewPromptEligibleCallCount++
            events += "isReviewPromptEligible"
            return eligible
        }

        override suspend fun recordReviewPromptAttempted() {
            recordReviewPromptAttemptedCallCount++
            events += "recordReviewPromptAttempted"
        }

        override suspend fun recordSuccessfulDetailsView() = Unit

        override suspend fun recordFavouriteAdded() = Unit
    }
}
